package com.modulersx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.modulersx.domain.dto.ModuleSaveDTO;
import com.modulersx.domain.po.ModulePO;
import com.modulersx.domain.vo.ModuleVO;
import com.modulersx.exception.BizException;
import com.modulersx.repository.ModuleMapper;
import com.modulersx.service.ModuleService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ModuleServiceImpl implements ModuleService {

    private final ModuleMapper moduleMapper;

    public ModuleServiceImpl(ModuleMapper moduleMapper) {
        this.moduleMapper = moduleMapper;
    }

    @Override
    public List<ModuleVO> listModules() {
        return moduleMapper.selectList(new LambdaQueryWrapper<ModulePO>()
                        .orderByAsc(ModulePO::getId))
                .stream()
                .map(this::toVO)
                .toList();
    }

    @Override
    public ModuleVO getModule(String code) {
        return toVO(requireModule(code));
    }

    @Override
    public ModuleVO createModule(ModuleSaveDTO dto) {
        validateForCreate(dto);
        if (existsByCode(dto.getCode())) {
            throw new BizException(400, "module code already exists");
        }
        ModulePO module = toPO(dto, null);
        moduleMapper.insert(module);
        return toVO(module);
    }

    @Override
    public ModuleVO updateModule(String code, ModuleSaveDTO dto) {
        ModulePO existing = requireModule(code);
        validateForUpdate(dto);
        existing.setName(dto.getName());
        existing.setType(dto.getType());
        existing.setStatus(dto.getStatus());
        existing.setRemark(dto.getRemark());
        moduleMapper.updateById(existing);
        return toVO(existing);
    }

    @Override
    public void deleteModule(String code) {
        ModulePO existing = requireModule(code);
        moduleMapper.deleteById(existing.getId());
    }

    private void validateForCreate(ModuleSaveDTO dto) {
        if (!StringUtils.hasText(dto.getCode())) {
            throw new BizException(400, "module code cannot be blank");
        }
        validateForUpdate(dto);
    }

    private void validateForUpdate(ModuleSaveDTO dto) {
        if (!StringUtils.hasText(dto.getName())) {
            throw new BizException(400, "module name cannot be blank");
        }
        if (!StringUtils.hasText(dto.getType())) {
            throw new BizException(400, "module type cannot be blank");
        }
        if (!StringUtils.hasText(dto.getStatus())) {
            throw new BizException(400, "module status cannot be blank");
        }
    }

    private ModulePO requireModule(String code) {
        ModulePO module = moduleMapper.selectOne(new LambdaQueryWrapper<ModulePO>()
                .eq(ModulePO::getCode, code)
                .last("limit 1"));
        if (module == null) {
            throw new BizException(404, "module not found");
        }
        return module;
    }

    private boolean existsByCode(String code) {
        return moduleMapper.selectCount(new LambdaQueryWrapper<ModulePO>()
                .eq(ModulePO::getCode, code)) > 0;
    }

    private ModulePO toPO(ModuleSaveDTO dto, Long id) {
        ModulePO module = new ModulePO();
        module.setId(id);
        module.setCode(dto.getCode());
        module.setName(dto.getName());
        module.setType(dto.getType());
        module.setStatus(dto.getStatus());
        module.setRemark(dto.getRemark());
        return module;
    }

    private ModuleVO toVO(ModulePO po) {
        return new ModuleVO(po.getCode(), po.getName(), po.getType(), po.getStatus(), po.getRemark());
    }
}
