package com.modulersx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.modulersx.domain.dto.ToolSaveDTO;
import com.modulersx.domain.po.ToolPO;
import com.modulersx.domain.vo.ToolVO;
import com.modulersx.exception.BizException;
import com.modulersx.repository.ToolMapper;
import com.modulersx.service.ToolService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ToolServiceImpl implements ToolService {

    private final ToolMapper toolMapper;

    public ToolServiceImpl(ToolMapper toolMapper) {
        this.toolMapper = toolMapper;
    }

    @Override
    public List<ToolVO> listTools() {
        return toolMapper.selectList(new LambdaQueryWrapper<ToolPO>()
                        .orderByAsc(ToolPO::getId))
                .stream()
                .map(this::toVO)
                .toList();
    }

    @Override
    public ToolVO getTool(Long id) {
        return toVO(requireTool(id));
    }

    @Override
    public ToolVO createTool(ToolSaveDTO dto) {
        validate(dto);
        ToolPO tool = toPO(dto, null);
        toolMapper.insert(tool);
        return toVO(tool);
    }

    @Override
    public ToolVO updateTool(Long id, ToolSaveDTO dto) {
        validate(dto);
        ToolPO tool = requireTool(id);
        tool.setName(dto.getName());
        tool.setCategory(dto.getCategory());
        tool.setUrl(dto.getUrl());
        tool.setDescription(dto.getDescription());
        tool.setStatus(dto.getStatus());
        toolMapper.updateById(tool);
        return toVO(tool);
    }

    @Override
    public void deleteTool(Long id) {
        ToolPO tool = requireTool(id);
        toolMapper.deleteById(tool.getId());
    }

    private void validate(ToolSaveDTO dto) {
        if (!StringUtils.hasText(dto.getName())) {
            throw new BizException(400, "tool name cannot be blank");
        }
        if (!StringUtils.hasText(dto.getCategory())) {
            throw new BizException(400, "tool category cannot be blank");
        }
        if (!StringUtils.hasText(dto.getStatus())) {
            throw new BizException(400, "tool status cannot be blank");
        }
    }

    private ToolPO requireTool(Long id) {
        ToolPO tool = toolMapper.selectById(id);
        if (tool == null) {
            throw new BizException(404, "tool not found");
        }
        return tool;
    }

    private ToolPO toPO(ToolSaveDTO dto, Long id) {
        ToolPO tool = new ToolPO();
        tool.setId(id);
        tool.setName(dto.getName());
        tool.setCategory(dto.getCategory());
        tool.setUrl(dto.getUrl());
        tool.setDescription(dto.getDescription());
        tool.setStatus(dto.getStatus());
        return tool;
    }

    private ToolVO toVO(ToolPO po) {
        return new ToolVO(po.getId(), po.getName(), po.getCategory(), po.getUrl(), po.getDescription(), po.getStatus());
    }
}
