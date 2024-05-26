package org.example.key;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@Conditional(RedisKeyLoadMatch.class)
public class UserProviderCacheKey extends RedisKeyBuilder{


    private static final String USER_INFO_KEY="userInfo";

    public String buildUserInfoKey(String userId) {
        return super.getPrefix()+USER_INFO_KEY+super.getSplit()+userId;
    }
}
