package org.example.key;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@Conditional(RedisKeyLoadMatch.class)
public class UserProviderCacheKey extends RedisKeyBuilder{


    private static final String USER_INFO_KEY="userInfo";

    private static final String USER_TAG_INFO_KEY="userTag";

    private static final String USER_TAG_LOCK_KEY="userTagLock";

    private static final String USER_PHONE_KEY="userPhone";

    private static final String USER_LOGIN_TOKEN_KEY="userLoginToken";

    private static final String USER_PHONE_LIST="userPhoneList";

    public String buildUserInfoKey(String userId) {
        return super.getPrefix()+USER_INFO_KEY+super.getSplit()+userId;
    }

    public String buildUserTagKey(Long userId) {
        return super.getPrefix()+USER_TAG_INFO_KEY+super.getSplit()+userId;
    }

    public String buildUserTagLockKey(Long userId) {
        return super.getPrefix()+USER_TAG_LOCK_KEY+super.getSplit()+userId;
    }

    public String buildUserPhoneKey(String phone) {
        return super.getPrefix()+USER_PHONE_KEY+super.getSplit()+phone;
    }

    public String buildUserLoginToken(String token) {
        return super.getPrefix()+USER_LOGIN_TOKEN_KEY+super.getSplit()+token;
    }

    public String buildUserPhoneList(Long userId) {
        return super.getPrefix()+USER_PHONE_LIST+super.getSplit()+userId;
    }
}
