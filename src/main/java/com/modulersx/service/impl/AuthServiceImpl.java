package com.modulersx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modulersx.domain.dto.LoginDTO;
import com.modulersx.domain.po.UserPO;
import com.modulersx.domain.vo.LoginVO;
import com.modulersx.domain.vo.UserInfoVO;
import com.modulersx.exception.BizException;
import com.modulersx.repository.UserMapper;
import com.modulersx.service.AuthService;
import java.time.Duration;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AuthServiceImpl implements AuthService {

    private static final String TOKEN_KEY_PREFIX = "module-rsx:auth:token:";

    private final UserMapper userMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;
    private final Duration tokenTtl;

    public AuthServiceImpl(
            UserMapper userMapper,
            StringRedisTemplate stringRedisTemplate,
            ObjectMapper objectMapper,
            @Value("${app.auth.token-ttl-minutes}") long tokenTtlMinutes) {
        this.userMapper = userMapper;
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
        this.tokenTtl = Duration.ofMinutes(tokenTtlMinutes);
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
        saveToken(token, userInfo);
        return new LoginVO(token, userInfo);
    }

    @Override
    public void logout(String token) {
        if (!StringUtils.hasText(token)) {
            throw new BizException(400, "token cannot be blank");
        }
        stringRedisTemplate.delete(buildTokenKey(token));
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
        String tokenKey = buildTokenKey(token);
        String userInfoJson = stringRedisTemplate.opsForValue().get(tokenKey);
        UserInfoVO userInfo = readUserInfo(userInfoJson);
        if (userInfo == null) {
            throw new BizException(401, "login expired or invalid token");
        }
        // 每次有效请求都刷新 token 过期时间，避免用户正常使用时突然掉线。
        stringRedisTemplate.expire(tokenKey, tokenTtl);
        return userInfo;
    }

    private UserInfoVO toUserInfo(UserPO user) {
        return new UserInfoVO(user.getUsername(), user.getNickname(), user.getStatus());
    }

    private void saveToken(String token, UserInfoVO userInfo) {
        try {
            // 登录态写入 Redis，多个后端实例可以共享同一份 token 数据。
            stringRedisTemplate.opsForValue().set(
                    buildTokenKey(token),
                    objectMapper.writeValueAsString(userInfo),
                    tokenTtl);
        } catch (JsonProcessingException ex) {
            throw new BizException(500, "failed to save login token");
        }
    }

    private UserInfoVO readUserInfo(String userInfoJson) {
        if (!StringUtils.hasText(userInfoJson)) {
            return null;
        }
        try {
            return objectMapper.readValue(userInfoJson, UserInfoVO.class);
        } catch (JsonProcessingException ex) {
            throw new BizException(401, "login expired or invalid token");
        }
    }

    private String buildTokenKey(String token) {
        return TOKEN_KEY_PREFIX + token;
    }
}
