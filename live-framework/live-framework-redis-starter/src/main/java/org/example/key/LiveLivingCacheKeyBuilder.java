package org.example.key;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@Conditional(RedisKeyLoadMatch.class)
public class LiveLivingCacheKeyBuilder extends RedisKeyBuilder{

    private static final String LIVE_ROOM_CACHE_KEY="liveRoomList";
    private static final String LIVE_ROOM_SINGLE_CACHE_KEY="liveRoom";

    private static final String LIVE_ROOM_CACHE_LOCK_KEY="liveRoomListCacheLock";



    public String buildLiveRoomCacheKey(Integer type) {
        return super.getPrefix()+LIVE_ROOM_CACHE_KEY+super.getSplit()+type;
    }
    public String buildLiveRoomSingleCacheKey(Integer roomId) {
        return super.getPrefix()+LIVE_ROOM_SINGLE_CACHE_KEY+super.getSplit()+roomId;
    }


    public String buildLiveRoomCacheLockKey( ) {
        return super.getPrefix()+LIVE_ROOM_CACHE_LOCK_KEY;
    }


}
