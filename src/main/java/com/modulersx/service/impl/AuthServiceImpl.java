package com.modulersx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.modulersx.domain.dto.LoginDTO;
import com.modulersx.domain.po.UserPO;
import com.modulersx.domain.vo.LoginVO;
import com.modulersx.domain.vo.UserInfoVO;
import com.modulersx.exception.BizException;
import com.modulersx.repository.UserMapper;
import com.modulersx.service.AuthService;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final Map<String, UserInfoVO> tokenStore = new ConcurrentHashMap<>();

    public AuthServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public LoginVO login(LoginDTO dto) {
        UserPO user = userMapper.selectOne(new LambdaQueryWrapper<UserPO>()
                .eq(UserPO::getUsername, dto.getUsername())
                .last("limit 1"));

        if (user == null || !dto.getPassword().equals(user.getPassword())) {
            throw new BizException(401, "username or password is incorrect");
        }

        if (!"enabled".equalsIgnoreCase(user.getStatus())) {
            throw new BizException(403, "user is disabled");
        }

        String token = UUID.randomUUID().toString().replace("-", "");
        UserInfoVO userInfo = toUserInfo(user);
        tokenStore.put(token, userInfo);
        return new LoginVO(token, userInfo);
    }

    @Override
    public void logout(String token) {
        if (!StringUtils.hasText(token)) {
            throw new BizException(400, "token cannot be blank");
        }
        tokenStore.remove(token);
    }

    @Override
    public UserInfoVO currentUser(String token) {
        return validateToken(token);
    }

    @Override
    public UserInfoVO validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            throw new BizException(401, "token is missing");
        }
        UserInfoVO userInfo = tokenStore.get(token);
        if (userInfo == null) {
            throw new BizException(401, "login expired or invalid token");
        }
        return userInfo;
    }

    private UserInfoVO toUserInfo(UserPO user) {
        return new UserInfoVO(user.getUsername(), user.getNickname(), user.getStatus());
    }
}
