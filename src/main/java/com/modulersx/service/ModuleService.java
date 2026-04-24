package com.modulersx.service;

import com.modulersx.domain.dto.ModuleSaveDTO;
import com.modulersx.domain.vo.ModuleVO;
import java.util.List;

public interface ModuleService {

    List<ModuleVO> listModules();

    ModuleVO getModule(String code);

    ModuleVO createModule(ModuleSaveDTO dto);

    ModuleVO updateModule(String code, ModuleSaveDTO dto);

    void deleteModule(String code);
}
