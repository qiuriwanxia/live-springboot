package org.example.key;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@Conditional(RedisKeyLoadMatch.class)
public class LiveImCoreCacheKey extends RedisKeyBuilder{


    private static final String LIVE_HEART_BEAT="liveHeadrtBeat";


    public String buildImHeartBeatKey(Long userId) {
        return super.getPrefix()+LIVE_HEART_BEAT+super.getSplit()+(userId%10000);
    }


}
