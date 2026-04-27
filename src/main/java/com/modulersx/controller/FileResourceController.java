package com.modulersx.controller;

import com.modulersx.common.response.ApiResponse;
import com.modulersx.domain.vo.FileResourceVO;
import com.modulersx.service.FileResourceService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileResourceController {

    private final FileResourceService fileResourceService;

    public FileResourceController(FileResourceService fileResourceService) {
        this.fileResourceService = fileResourceService;
    }

    @PostMapping("/upload")
    public ApiResponse<FileResourceVO> uploadImage(@RequestParam("file") MultipartFile file) {
        return ApiResponse.success(fileResourceService.uploadImage(file));
    }
}
