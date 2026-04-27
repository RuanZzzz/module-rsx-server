package com.modulersx.config;

import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;
    private final String uploadRootDir;
    private final String uploadAccessPath;

    public WebMvcConfig(
            LoginInterceptor loginInterceptor,
            @Value("${app.upload.root-dir:./uploads}") String uploadRootDir,
            @Value("${app.upload.access-path:/uploads}") String uploadAccessPath) {
        this.loginInterceptor = loginInterceptor;
        this.uploadRootDir = uploadRootDir;
        this.uploadAccessPath = uploadAccessPath;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/auth/login",
                        "/hello");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String pattern = normalizeAccessPath(uploadAccessPath) + "/**";
        String location = Path.of(uploadRootDir).toAbsolutePath().normalize().toUri().toString();
        if (!location.endsWith("/")) {
            location = location + "/";
        }

        // 把本地上传目录暴露成 HTTP 静态访问路径，例如 /uploads/2026/04/27/a.png。
        registry.addResourceHandler(pattern)
                .addResourceLocations(location);
    }

    private String normalizeAccessPath(String value) {
        String path = value == null || value.isBlank() ? "/uploads" : value.trim();
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }
}
