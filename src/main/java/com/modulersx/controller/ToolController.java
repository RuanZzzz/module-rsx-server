package com.modulersx.controller;

import com.modulersx.common.response.ApiResponse;
import com.modulersx.domain.dto.ToolSaveDTO;
import com.modulersx.domain.vo.ToolVO;
import com.modulersx.service.ToolService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tools")
public class ToolController {

    private final ToolService toolService;

    public ToolController(ToolService toolService) {
        this.toolService = toolService;
    }

    @GetMapping
    public ApiResponse<List<ToolVO>> listTools() {
        return ApiResponse.success(toolService.listTools());
    }

    @GetMapping("/detail")
    public ApiResponse<ToolVO> getTool(@RequestParam Long id) {
        return ApiResponse.success(toolService.getTool(id));
    }

    @PostMapping("/create")
    public ApiResponse<ToolVO> createTool(@RequestBody ToolSaveDTO dto) {
        return ApiResponse.success(toolService.createTool(dto));
    }

    @PostMapping("/update")
    public ApiResponse<ToolVO> updateTool(@RequestParam Long id, @RequestBody ToolSaveDTO dto) {
        return ApiResponse.success(toolService.updateTool(id, dto));
    }

    @PostMapping("/delete")
    public ApiResponse<Void> deleteTool(@RequestParam Long id) {
        toolService.deleteTool(id);
        return ApiResponse.success(null);
    }
}
