package org.example.service.impl;

import jakarta.annotation.Resource;

import org.example.key.LiveAccountCacheKey;
import org.example.key.UserProviderCacheKey;
import org.example.service.IAccountTokenService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class IAccountTokenServiceImpl implements IAccountTokenService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private LiveAccountCacheKey liveAccountCacheKey;

    @Override
    public String createAndSaveLoginToken(Long userId) {
        String token = UUID.randomUUID().toString();
        String buildUserLoginToken = liveAccountCacheKey.buildUserLoginToken(token);
        redisTemplate.opsForValue().set(buildUserLoginToken, userId, 24, TimeUnit.HOURS);
        return token;
    }

    @Override
    public Long getUserIdByToken(String token) {
        String buildUserLoginToken = liveAccountCacheKey.buildUserLoginToken(token);
        Long userId = (long)redisTemplate.opsForValue().get(buildUserLoginToken);
        return userId;
    }
}
