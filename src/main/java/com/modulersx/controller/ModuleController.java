package com.modulersx.controller;

import com.modulersx.common.response.ApiResponse;
import com.modulersx.domain.dto.ModuleSaveDTO;
import com.modulersx.domain.vo.ModuleVO;
import com.modulersx.service.ModuleService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/modules")
public class ModuleController {

    private final ModuleService moduleService;

    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @GetMapping
    public ApiResponse<List<ModuleVO>> listModules() {
        return ApiResponse.success(moduleService.listModules());
    }

    @GetMapping("/detail")
    public ApiResponse<ModuleVO> getModule(@RequestParam String code) {
        return ApiResponse.success(moduleService.getModule(code));
    }

    @PostMapping("/create")
    public ApiResponse<ModuleVO> createModule(@RequestBody ModuleSaveDTO dto) {
        return ApiResponse.success(moduleService.createModule(dto));
    }

    @PostMapping("/update")
    public ApiResponse<ModuleVO> updateModule(@RequestParam String code, @RequestBody ModuleSaveDTO dto) {
        return ApiResponse.success(moduleService.updateModule(code, dto));
    }

    @PostMapping("/delete")
    public ApiResponse<Void> deleteModule(@RequestParam String code) {
        moduleService.deleteModule(code);
        return ApiResponse.success(null);
    }
}
