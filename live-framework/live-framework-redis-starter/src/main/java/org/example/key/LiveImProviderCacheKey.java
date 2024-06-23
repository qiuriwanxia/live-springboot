package org.example.key;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@Conditional(RedisKeyLoadMatch.class)
public class LiveImProviderCacheKey extends RedisKeyBuilder{


    private static final String LIVE_IM_TOKEN="liveImToken";


    public String buildImTokenCodeKey(String userId) {
        return super.getPrefix()+LIVE_IM_TOKEN+super.getSplit()+userId;
    }


}
