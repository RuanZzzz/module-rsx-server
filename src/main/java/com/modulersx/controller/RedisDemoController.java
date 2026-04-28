package com.modulersx.controller;

import com.modulersx.common.response.ApiResponse;
import com.modulersx.service.RedisDemoService;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/redis")
public class RedisDemoController {

    private final RedisDemoService redisDemoService;

    public RedisDemoController(RedisDemoService redisDemoService) {
        this.redisDemoService = redisDemoService;
    }

    @GetMapping("/ping")
    public ApiResponse<Map<String, Object>> ping() {
        return ApiResponse.success(redisDemoService.ping());
    }

    @PostMapping("/demo")
    public ApiResponse<Map<String, Object>> setDemoValue(@RequestParam String value) {
        return ApiResponse.success(redisDemoService.setDemoValue(value));
    }

    @GetMapping("/demo")
    public ApiResponse<Map<String, Object>> getDemoValue() {
        return ApiResponse.success(redisDemoService.getDemoValue());
    }
}
