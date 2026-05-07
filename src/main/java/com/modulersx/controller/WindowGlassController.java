package com.modulersx.controller;

import com.modulersx.common.response.ApiResponse;
import com.modulersx.domain.dto.WindowDesignSaveDTO;
import com.modulersx.domain.vo.WindowDesignVO;
import com.modulersx.domain.vo.WindowTemplateVO;
import com.modulersx.service.WindowGlassService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/window-glass")
public class WindowGlassController {

    private final WindowGlassService windowGlassService;

    public WindowGlassController(WindowGlassService windowGlassService) {
        this.windowGlassService = windowGlassService;
    }

    @GetMapping("/templates")
    public ApiResponse<List<WindowTemplateVO>> listTemplates() {
        return ApiResponse.success(windowGlassService.listTemplates());
    }

    @GetMapping("/designs")
    public ApiResponse<List<WindowDesignVO>> listDesigns() {
        return ApiResponse.success(windowGlassService.listDesigns());
    }

    @PostMapping("/calculate")
    public ApiResponse<WindowDesignVO> calculate(@RequestBody WindowDesignSaveDTO dto) {
        return ApiResponse.success(windowGlassService.calculate(dto));
    }

    @PostMapping("/designs/create")
    public ApiResponse<WindowDesignVO> createDesign(@RequestBody WindowDesignSaveDTO dto) {
        return ApiResponse.success(windowGlassService.createDesign(dto));
    }
}
