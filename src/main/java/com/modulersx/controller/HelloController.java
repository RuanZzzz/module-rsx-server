package com.modulersx.controller;

import com.modulersx.common.response.ApiResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public ApiResponse<Map<String, Object>> hello() {
        return ApiResponse.success(Map.of(
                "message", "module-rsx server is running",
                "stage", "spring-boot-init",
                "instance", currentInstance()));
    }

    private String currentInstance() {
        try {
            // 在容器里 hostname 通常就是容器 ID，用来区分当前请求打到了哪个后端实例。
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            return "unknown";
        }
    }
}
