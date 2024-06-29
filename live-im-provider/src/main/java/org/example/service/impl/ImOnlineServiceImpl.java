package org.example.service.impl;

import jakarta.annotation.Resource;
import org.example.service.ImOnlineService;
import org.springframework.data.redis.core.StringRedisTemplate;

public class ImOnlineServiceImpl implements ImOnlineService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public boolean isOnine(Long userId) {
        return stringRedisTemplate.hasKey("liveHeadrtBeat:"+userId);
    }

}
