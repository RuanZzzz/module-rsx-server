package com.modulersx.controller;

import com.modulersx.common.response.ApiResponse;
import com.modulersx.domain.dto.ModuleSaveDTO;
import com.modulersx.domain.vo.ModuleVO;
import com.modulersx.service.ModuleService;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @GetMapping("/{code}")
    public ApiResponse<ModuleVO> getModule(@PathVariable String code) {
        return ApiResponse.success(moduleService.getModule(code));
    }

    @PostMapping
    public ApiResponse<ModuleVO> createModule(@RequestBody ModuleSaveDTO dto) {
        return ApiResponse.success(moduleService.createModule(dto));
    }

    @PutMapping("/{code}")
    public ApiResponse<ModuleVO> updateModule(@PathVariable String code, @RequestBody ModuleSaveDTO dto) {
        return ApiResponse.success(moduleService.updateModule(code, dto));
    }

    @DeleteMapping("/{code}")
    public ApiResponse<Void> deleteModule(@PathVariable String code) {
        moduleService.deleteModule(code);
        return ApiResponse.success(null);
    }
}
