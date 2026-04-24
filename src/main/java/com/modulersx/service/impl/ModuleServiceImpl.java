package com.modulersx.service.impl;

import com.modulersx.domain.dto.ModuleSaveDTO;
import com.modulersx.domain.vo.ModuleVO;
import com.modulersx.exception.BizException;
import com.modulersx.service.ModuleService;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ModuleServiceImpl implements ModuleService {

    private final Map<String, ModuleVO> moduleStore = new LinkedHashMap<>();

    public ModuleServiceImpl() {
        save(new ModuleVO("module", "模块管理", "system", "enabled", "管理系统内可用模块"));
        save(new ModuleVO("tool", "教学工具", "business", "enabled", "管理教学工具内容"));
        save(new ModuleVO("article", "文章管理", "content", "disabled", "后续会接入文章与图片上传"));
        save(new ModuleVO("order", "订单管理", "business", "disabled", "后续用于订单状态跟踪"));
    }

    @Override
    public List<ModuleVO> listModules() {
        return new ArrayList<>(moduleStore.values());
    }

    @Override
    public ModuleVO getModule(String code) {
        return requireModule(code);
    }

    @Override
    public ModuleVO createModule(ModuleSaveDTO dto) {
        validateForCreate(dto);
        if (moduleStore.containsKey(dto.getCode())) {
            throw new BizException(400, "module code already exists");
        }
        ModuleVO module = toModule(dto, dto.getCode());
        save(module);
        return module;
    }

    @Override
    public ModuleVO updateModule(String code, ModuleSaveDTO dto) {
        ModuleVO existing = requireModule(code);
        validateForUpdate(dto);
        existing.setName(dto.getName());
        existing.setType(dto.getType());
        existing.setStatus(dto.getStatus());
        existing.setRemark(dto.getRemark());
        save(existing);
        return existing;
    }

    @Override
    public void deleteModule(String code) {
        ModuleVO removed = moduleStore.remove(code);
        if (removed == null) {
            throw new BizException(404, "module not found");
        }
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

    private ModuleVO requireModule(String code) {
        ModuleVO module = moduleStore.get(code);
        if (module == null) {
            throw new BizException(404, "module not found");
        }
        return module;
    }

    private ModuleVO toModule(ModuleSaveDTO dto, String code) {
        return new ModuleVO(code, dto.getName(), dto.getType(), dto.getStatus(), dto.getRemark());
    }

    private void save(ModuleVO module) {
        moduleStore.put(module.getCode(), module);
    }
}
