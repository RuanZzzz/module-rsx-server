package com.modulersx.controller;

import com.modulersx.common.response.ApiResponse;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public ApiResponse<Map<String, Object>> hello() {
        return ApiResponse.success(Map.of(
                "message", "module-rsx server is running",
                "stage", "spring-boot-init"));
    }
}
