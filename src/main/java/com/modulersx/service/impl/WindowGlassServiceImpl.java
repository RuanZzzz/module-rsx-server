package com.modulersx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modulersx.common.log.AppLoggers;
import com.modulersx.domain.dto.WindowDesignSaveDTO;
import com.modulersx.domain.po.WindowDesignPO;
import com.modulersx.domain.po.WindowGlassPiecePO;
import com.modulersx.domain.po.WindowTemplatePO;
import com.modulersx.domain.vo.WindowDesignVO;
import com.modulersx.domain.vo.WindowGlassPieceVO;
import com.modulersx.domain.vo.WindowTemplateVO;
import com.modulersx.exception.BizException;
import com.modulersx.repository.WindowDesignMapper;
import com.modulersx.repository.WindowGlassPieceMapper;
import com.modulersx.repository.WindowTemplateMapper;
import com.modulersx.service.WindowGlassService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class WindowGlassServiceImpl implements WindowGlassService {

    private static final Logger PROCESS_LOG = LogManager.getLogger(AppLoggers.PROCESS);
    private static final BigDecimal SQUARE_CENTIMETER_TO_METER = new BigDecimal("10000");

    private final WindowTemplateMapper templateMapper;
    private final WindowDesignMapper designMapper;
    private final WindowGlassPieceMapper pieceMapper;
    private final ObjectMapper objectMapper;

    public WindowGlassServiceImpl(
            WindowTemplateMapper templateMapper,
            WindowDesignMapper designMapper,
            WindowGlassPieceMapper pieceMapper,
            ObjectMapper objectMapper) {
        this.templateMapper = templateMapper;
        this.designMapper = designMapper;
        this.pieceMapper = pieceMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<WindowTemplateVO> listTemplates() {
        return templateMapper.selectList(new LambdaQueryWrapper<WindowTemplatePO>()
                        .eq(WindowTemplatePO::getStatus, "enabled")
                        .orderByAsc(WindowTemplatePO::getId))
                .stream()
                .map(this::toTemplateVO)
                .toList();
    }

    @Override
    public List<WindowDesignVO> listDesigns() {
        return designMapper.selectList(new LambdaQueryWrapper<WindowDesignPO>()
                        .orderByDesc(WindowDesignPO::getId))
                .stream()
                .map(this::toDesignVO)
                .toList();
    }

    @Override
    public WindowDesignVO calculate(WindowDesignSaveDTO dto) {
        WindowTemplatePO template = requireTemplate(dto.getTemplateCode());
        return buildCalculatedDesign(dto, template);
    }

    @Override
    @Transactional
    public WindowDesignVO createDesign(WindowDesignSaveDTO dto) {
        WindowTemplatePO template = requireTemplate(dto.getTemplateCode());
        WindowDesignVO calculated = buildCalculatedDesign(dto, template);
        WindowDesignPO design = new WindowDesignPO();
        design.setName(dto.getName());
        design.setTemplateId(template.getId());
        design.setTemplateCode(template.getCode());
        design.setCustomerName(dto.getCustomerName());
        design.setParamsJson(writeJson(dto.getParams()));
        design.setTotalArea(calculated.getTotalArea());
        design.setRemark(dto.getRemark());
        designMapper.insert(design);

        for (WindowGlassPieceVO pieceVO : calculated.getPieces()) {
            WindowGlassPiecePO piece = new WindowGlassPiecePO();
            piece.setDesignId(design.getId());
            piece.setName(pieceVO.getName());
            piece.setShapeType(pieceVO.getShapeType());
            piece.setShapeDataJson(pieceVO.getShapeDataJson());
            piece.setArea(pieceVO.getArea());
            piece.setSortOrder(pieceVO.getSortOrder());
            pieceMapper.insert(piece);
        }

        PROCESS_LOG.info("window design saved, id={}, template={}, totalArea={}", design.getId(), template.getCode(), calculated.getTotalArea());
        return toDesignVO(design);
    }

    private WindowDesignVO buildCalculatedDesign(WindowDesignSaveDTO dto, WindowTemplatePO template) {
        validateDesign(dto);
        List<WindowGlassPieceVO> pieces = calculatePieces(template.getShapeType(), dto.getParams());
        BigDecimal totalArea = pieces.stream()
                .map(WindowGlassPieceVO::getArea)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(4, RoundingMode.HALF_UP);

        WindowDesignVO vo = new WindowDesignVO();
        vo.setName(dto.getName());
        vo.setTemplateCode(template.getCode());
        vo.setTemplateName(template.getName());
        vo.setShapeType(template.getShapeType());
        vo.setCustomerName(dto.getCustomerName());
        vo.setParams(dto.getParams());
        vo.setTotalArea(totalArea);
        vo.setRemark(dto.getRemark());
        vo.setPieces(pieces);
        return vo;
    }

    private List<WindowGlassPieceVO> calculatePieces(String shapeType, Map<String, BigDecimal> params) {
        return switch (shapeType) {
            case "RECTANGLE" -> List.of(piece("整块玻璃", shapeType, params, rectangleArea(params, "width", "height"), 1));
            case "TRIANGLE" -> List.of(piece("三角玻璃", shapeType, params, triangleArea(params, "base", "height"), 1));
            case "TRAPEZOID" -> List.of(piece("梯形玻璃", shapeType, params, trapezoidArea(params), 1));
            case "L_SHAPE" -> calculateLShape(params);
            case "CORNER" -> calculateCorner(params);
            case "CUSTOM_POLYGON" -> calculateCustomPolygon(params);
            default -> throw new BizException(400, "unsupported window shape type");
        };
    }

    private List<WindowGlassPieceVO> calculateLShape(Map<String, BigDecimal> params) {
        BigDecimal width = positive(params, "width");
        BigDecimal height = positive(params, "height");
        BigDecimal cutWidth = positive(params, "cutWidth");
        BigDecimal cutHeight = positive(params, "cutHeight");
        if (cutWidth.compareTo(width) >= 0 || cutHeight.compareTo(height) >= 0) {
            throw new BizException(400, "L shape cut size must be smaller than total size");
        }
        BigDecimal area = toSquareMeter(width.multiply(height).subtract(cutWidth.multiply(cutHeight)));
        return List.of(piece("L 型异形玻璃", "L_SHAPE", params, area, 1));
    }

    private List<WindowGlassPieceVO> calculateCorner(Map<String, BigDecimal> params) {
        BigDecimal frontWidth = positive(params, "frontWidth");
        BigDecimal sideWidth = positive(params, "sideWidth");
        BigDecimal height = positive(params, "height");
        List<WindowGlassPieceVO> pieces = new ArrayList<>();
        pieces.add(piece("正面玻璃", "RECTANGLE", Map.of("width", frontWidth, "height", height), toSquareMeter(frontWidth.multiply(height)), 1));
        pieces.add(piece("侧面玻璃", "RECTANGLE", Map.of("width", sideWidth, "height", height), toSquareMeter(sideWidth.multiply(height)), 2));
        return pieces;
    }

    private List<WindowGlassPieceVO> calculateCustomPolygon(Map<String, BigDecimal> params) {
        int pointCount = positive(params, "pointCount").intValue();
        if (pointCount < 3 || pointCount > 12) {
            throw new BizException(400, "custom polygon point count must be between 3 and 12");
        }
        BigDecimal doubleArea = BigDecimal.ZERO;
        for (int i = 1; i <= pointCount; i++) {
            int next = i == pointCount ? 1 : i + 1;
            BigDecimal x1 = nonNegative(params, "x" + i);
            BigDecimal y1 = nonNegative(params, "y" + i);
            BigDecimal x2 = nonNegative(params, "x" + next);
            BigDecimal y2 = nonNegative(params, "y" + next);
            doubleArea = doubleArea.add(x1.multiply(y2).subtract(x2.multiply(y1)));
        }
        // 自定义多边形用鞋带公式计算面积，坐标单位为 cm，最终统一换算为平方米。
        BigDecimal area = toSquareMeter(doubleArea.abs().divide(new BigDecimal("2"), 4, RoundingMode.HALF_UP));
        return List.of(piece("自定义多边形玻璃", "CUSTOM_POLYGON", params, area, 1));
    }

    private BigDecimal rectangleArea(Map<String, BigDecimal> params, String widthKey, String heightKey) {
        return toSquareMeter(positive(params, widthKey).multiply(positive(params, heightKey)));
    }

    private BigDecimal triangleArea(Map<String, BigDecimal> params, String baseKey, String heightKey) {
        return toSquareMeter(positive(params, baseKey).multiply(positive(params, heightKey)).divide(new BigDecimal("2"), 4, RoundingMode.HALF_UP));
    }

    private BigDecimal trapezoidArea(Map<String, BigDecimal> params) {
        BigDecimal topWidth = positive(params, "topWidth");
        BigDecimal bottomWidth = positive(params, "bottomWidth");
        BigDecimal height = positive(params, "height");
        return toSquareMeter(topWidth.add(bottomWidth).multiply(height).divide(new BigDecimal("2"), 4, RoundingMode.HALF_UP));
    }

    private WindowGlassPieceVO piece(String name, String shapeType, Map<String, BigDecimal> shapeData, BigDecimal area, Integer sortOrder) {
        return new WindowGlassPieceVO(name, shapeType, writeJson(shapeData), area, sortOrder);
    }

    private BigDecimal toSquareMeter(BigDecimal squareCentimeter) {
        return squareCentimeter.divide(SQUARE_CENTIMETER_TO_METER, 4, RoundingMode.HALF_UP);
    }

    private BigDecimal positive(Map<String, BigDecimal> params, String key) {
        if (params == null || params.get(key) == null || params.get(key).compareTo(BigDecimal.ZERO) <= 0) {
            throw new BizException(400, "window param " + key + " must be greater than 0");
        }
        return params.get(key);
    }

    private BigDecimal nonNegative(Map<String, BigDecimal> params, String key) {
        if (params == null || params.get(key) == null || params.get(key).compareTo(BigDecimal.ZERO) < 0) {
            throw new BizException(400, "window param " + key + " cannot be negative");
        }
        return params.get(key);
    }

    private void validateDesign(WindowDesignSaveDTO dto) {
        if (!StringUtils.hasText(dto.getName())) {
            throw new BizException(400, "window design name cannot be blank");
        }
        if (!StringUtils.hasText(dto.getTemplateCode())) {
            throw new BizException(400, "window template code cannot be blank");
        }
        if (dto.getParams() == null || dto.getParams().isEmpty()) {
            throw new BizException(400, "window params cannot be empty");
        }
    }

    private WindowTemplatePO requireTemplate(String code) {
        WindowTemplatePO template = templateMapper.selectOne(new LambdaQueryWrapper<WindowTemplatePO>()
                .eq(WindowTemplatePO::getCode, code)
                .eq(WindowTemplatePO::getStatus, "enabled")
                .last("limit 1"));
        if (template == null) {
            throw new BizException(404, "window template not found");
        }
        return template;
    }

    private WindowTemplateVO toTemplateVO(WindowTemplatePO po) {
        return new WindowTemplateVO(po.getId(), po.getCode(), po.getName(), po.getShapeType(), po.getDescription(), po.getSchemaJson(), po.getStatus());
    }

    private WindowDesignVO toDesignVO(WindowDesignPO po) {
        WindowTemplatePO template = requireTemplate(po.getTemplateCode());
        WindowDesignVO vo = new WindowDesignVO();
        vo.setId(po.getId());
        vo.setName(po.getName());
        vo.setTemplateCode(po.getTemplateCode());
        vo.setTemplateName(template.getName());
        vo.setShapeType(template.getShapeType());
        vo.setCustomerName(po.getCustomerName());
        vo.setParams(readParams(po.getParamsJson()));
        vo.setTotalArea(po.getTotalArea());
        vo.setRemark(po.getRemark());
        vo.setCreatedAt(po.getCreatedAt());
        vo.setUpdatedAt(po.getUpdatedAt());
        vo.setPieces(listPieces(po.getId()));
        return vo;
    }

    private List<WindowGlassPieceVO> listPieces(Long designId) {
        return pieceMapper.selectList(new LambdaQueryWrapper<WindowGlassPiecePO>()
                        .eq(WindowGlassPiecePO::getDesignId, designId)
                        .orderByAsc(WindowGlassPiecePO::getSortOrder))
                .stream()
                .map(piece -> new WindowGlassPieceVO(piece.getName(), piece.getShapeType(), piece.getShapeDataJson(), piece.getArea(), piece.getSortOrder()))
                .toList();
    }

    private Map<String, BigDecimal> readParams(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new BizException(500, "window params json parse failed");
        }
    }

    private String writeJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new BizException(500, "window json serialize failed");
        }
    }
}
