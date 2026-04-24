package com.modulersx.controller;

import com.modulersx.common.response.ApiResponse;
import com.modulersx.domain.dto.LoginDTO;
import com.modulersx.domain.vo.LoginVO;
import com.modulersx.domain.vo.UserInfoVO;
import com.modulersx.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        return ApiResponse.success(authService.login(dto));
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestHeader("X-Token") String token) {
        authService.logout(token);
        return ApiResponse.success(null);
    }

    @GetMapping("/me")
    public ApiResponse<UserInfoVO> currentUser(@RequestHeader("X-Token") String token) {
        return ApiResponse.success(authService.currentUser(token));
    }
}
