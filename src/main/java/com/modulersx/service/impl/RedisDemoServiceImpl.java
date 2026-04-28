package com.modulersx.service.impl;

import com.modulersx.exception.BizException;
import com.modulersx.service.RedisDemoService;
import java.time.Duration;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class RedisDemoServiceImpl implements RedisDemoService {

    private static final String DEMO_KEY = "module-rsx:demo";

    private final StringRedisTemplate stringRedisTemplate;
    private final RedisConnectionFactory redisConnectionFactory;
    private final String redisHost;
    private final int redisPort;

    public RedisDemoServiceImpl(
            StringRedisTemplate stringRedisTemplate,
            RedisConnectionFactory redisConnectionFactory,
            @Value("${spring.data.redis.host}") String redisHost,
            @Value("${spring.data.redis.port}") int redisPort) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisConnectionFactory = redisConnectionFactory;
        this.redisHost = redisHost;
        this.redisPort = redisPort;
    }

    @Override
    public Map<String, Object> ping() {
        // 直接向 Redis 发送 PING，用来验证 Spring Boot 是否真的连到了 Redis 容器。
        try (RedisConnection connection = redisConnectionFactory.getConnection()) {
            return Map.of(
                    "pong", connection.ping(),
                    "host", redisHost,
                    "port", redisPort);
        }
    }

    @Override
    public Map<String, Object> setDemoValue(String value) {
        if (!StringUtils.hasText(value)) {
            throw new BizException(400, "value cannot be blank");
        }

        // 给演示 key 设置过期时间，避免学习测试数据长期堆在 Redis 里。
        stringRedisTemplate.opsForValue().set(DEMO_KEY, value, Duration.ofMinutes(10));
        return Map.of(
                "key", DEMO_KEY,
                "value", value,
                "ttlSeconds", 600);
    }

    @Override
    public Map<String, Object> getDemoValue() {
        String value = stringRedisTemplate.opsForValue().get(DEMO_KEY);
        Long ttl = stringRedisTemplate.getExpire(DEMO_KEY);
        return Map.of(
                "key", DEMO_KEY,
                "value", value == null ? "" : value,
                "ttlSeconds", ttl == null ? -2 : ttl);
    }
}
