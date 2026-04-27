package com.modulersx.service;

import com.modulersx.domain.dto.LoginDTO;
import com.modulersx.domain.vo.LoginVO;
import com.modulersx.domain.vo.UserInfoVO;

public interface AuthService {

    LoginVO login(LoginDTO dto);

    void logout(String token);

    UserInfoVO currentUser(String token);

    UserInfoVO validateToken(String token);
}
