package org.example.key;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@Conditional(RedisKeyLoadMatch.class)
public class LiveAccountCacheKey extends RedisKeyBuilder{



    private static final String USER_LOGIN_TOKEN_KEY="userLoginToken";

    private static final String USER_PHONE_LIST="userPhoneList";

    public String buildUserLoginToken(String token) {
        return super.getPrefix()+USER_LOGIN_TOKEN_KEY+super.getSplit()+token;
    }

    public String buildUserPhoneList(Long userId) {
        return super.getPrefix()+USER_PHONE_LIST+super.getSplit()+userId;
    }
}
