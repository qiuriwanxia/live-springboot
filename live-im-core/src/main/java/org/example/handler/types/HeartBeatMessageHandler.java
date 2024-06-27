package org.example.handler.types;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.constant.ImMessageCacheGap;
import org.example.constant.ImMessageConstans;
import org.example.dto.ImMessageBody;
import org.example.handler.ImMessageStrategy;
import org.example.key.LiveImCoreCacheKey;
import org.example.message.ImMessage;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class HeartBeatMessageHandler implements ImMessageStrategy {


    @Resource
    private RedisTemplate<String,Object> redisTemplate;


    @Resource
    private LiveImCoreCacheKey liveImCoreCacheKey;


    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public void handleMessage(ChannelHandlerContext channelHandlerContext, ImMessage imMessage) {
        System.out.println("开始处理心跳消息");


        byte[] body = imMessage.getBody();

        if (body==null||body.length==0){
            channelHandlerContext.close();
        }

        ImMessageBody imMessageBody = JSON.parseObject(new String(body), ImMessageBody.class);

        Long userId = imMessageBody.getUserId();
        String appid = imMessageBody.getAppid();

        String redisKey = liveImCoreCacheKey.buildImHeartBeatKey(userId);

        redisTemplate.opsForZSet().add(redisKey,userId,System.currentTimeMillis());

        //删除超时的
        redisTemplate.opsForZSet().removeRangeByScore(redisKey,0,System.currentTimeMillis()- ImMessageCacheGap.VALUE*2);

        String cacheKey = ImMessageConstans.USER_BIND_IP_CACHE_KEY + userId + ":" + appid;
        stringRedisTemplate.expire(cacheKey,ImMessageCacheGap.VALUE*2, TimeUnit.SECONDS);


        //返回消息
        ImMessageBody imMessageBody1 = ImMessageBody.buildSuccess(userId, imMessageBody.getAppid(), imMessageBody.getToken());

        imMessage.setBody(JSON.toJSONBytes(imMessageBody1));

        channelHandlerContext.fireChannelRead(imMessage);

    }

}
