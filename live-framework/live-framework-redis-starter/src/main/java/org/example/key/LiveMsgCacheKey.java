package org.example.key;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@Conditional(RedisKeyLoadMatch.class)
public class LiveMsgCacheKey extends RedisKeyBuilder{


    private static final String LIVE_MSG_CODE="liveMsgCode";


    public String buildMsgCodeKey(String phone) {
        return super.getPrefix()+LIVE_MSG_CODE+super.getSplit()+phone;
    }


}
