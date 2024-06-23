package org.example.service.impl;

import com.alibaba.nacos.common.utils.UuidUtils;
import jakarta.annotation.Resource;
import org.example.key.LiveImProviderCacheKey;
import org.example.service.ImTokenService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ImTokenServiceImpl implements ImTokenService {


    @Resource
    private LiveImProviderCacheKey liveImProviderCacheKey;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public String createToken(Long userId,String appid) {

        String token = UUID.randomUUID().toString()+"%"+appid;

        String tokenCodeKey = liveImProviderCacheKey.buildImTokenCodeKey(token);

        redisTemplate.opsForValue().set(tokenCodeKey,userId,5, TimeUnit.MINUTES);

        return token;

    }

    @Override
    public Long getUserId(String token) {
        String tokenCodeKey = liveImProviderCacheKey.buildImTokenCodeKey(token);

        Number number = (Number) redisTemplate.opsForValue().get(tokenCodeKey);

        if (number==null){
            return null;
        }

        long userId = number.longValue();

        return userId;
    }

}
