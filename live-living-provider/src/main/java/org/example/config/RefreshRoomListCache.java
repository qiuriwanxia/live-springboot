package org.example.config;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.dao.pojo.TLivingRoomPo;
import org.example.dto.TLivingRoomDTO;
import org.example.enums.LivingEnum;
import org.example.key.LiveLivingCacheKeyBuilder;
import org.example.service.TLivingRoomService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RefreshRoomListCache implements InitializingBean{

    public RefreshRoomListCache() {
    }

    private ScheduledThreadPoolExecutor scheduledPool = new ScheduledThreadPoolExecutor(1);

    @Resource
    private LiveLivingCacheKeyBuilder liveLivingCacheKeyBuilder;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private TLivingRoomService tLivingRoomService;



    @Override
    public void afterPropertiesSet() throws Exception {
        scheduledPool.scheduleAtFixedRate(new RefreshRoomListJon(),3000,1000, TimeUnit.MILLISECONDS);
    }

    class RefreshRoomListJon implements Runnable{


        @Override
        public void run() {

            try {
                String liveRoomCacheLockKey = liveLivingCacheKeyBuilder.buildLiveRoomCacheLockKey();

                //加锁
                boolean lock = redisTemplate.opsForValue().setIfAbsent(liveRoomCacheLockKey, 1, 1, TimeUnit.SECONDS);

                if (lock){
                    refreshRoomList(LivingEnum.DEFAULT_ROOM_TYE.getCode());
                    refreshRoomList(LivingEnum.PK_ROOM_TYE.getCode());
                }
            } catch (Exception e) {
                log.error("出现异常 {}",e);
                throw new RuntimeException(e);
            }

        }

        private void refreshRoomList(Integer type) {
            String buildLiveRoomCacheKey = liveLivingCacheKeyBuilder.buildLiveRoomCacheKey(type);
            String buildLiveRoomCacheKeyTemp = buildLiveRoomCacheKey+"_temp";
            List<TLivingRoomDTO> livingRoomDTOList = tLivingRoomService.listByType(type);
            if (livingRoomDTOList.isEmpty()){
                return;
            }
            for (TLivingRoomDTO tLivingRoomDTO : livingRoomDTOList) {
                redisTemplate.opsForList().rightPushAll(buildLiveRoomCacheKeyTemp,tLivingRoomDTO);
            }
            redisTemplate.rename(buildLiveRoomCacheKeyTemp,buildLiveRoomCacheKey);
            redisTemplate.delete(buildLiveRoomCacheKeyTemp);
        }

    }



}
