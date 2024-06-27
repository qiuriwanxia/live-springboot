package org.example.handler.types;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.cache.ChannelHandlerContextCache;
import org.example.constant.ImMessageConstans;
import org.example.dto.ImMessageBody;
import org.example.handler.ImMessageStrategy;
import org.example.interfaces.ImTokenRpc;
import org.example.message.ImMessage;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoginOutMessageHandler implements ImMessageStrategy {

    @Resource
    private ImTokenRpc imTokenRpc;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public void handleMessage(ChannelHandlerContext channelHandlerContext, ImMessage imMessage) {
        log.info("开始处理登出消息");

        byte[] body = imMessage.getBody();

        if (body==null||body.length==0){
            channelHandlerContext.close();
        }

        ImMessageBody imMessageBody = JSON.parseObject(new String(body), ImMessageBody.class);


        Long userId = imMessageBody.getUserId();
        String appid = imMessageBody.getAppid();
        String token = imMessageBody.getToken();

        if (userId==null){
            userId = imTokenRpc.getUserId(token);
        }

        //删除缓存
        ChannelHandlerContextCache.remove(userId);

        String cacheKey = ImMessageConstans.USER_BIND_IP_CACHE_KEY + userId + ":" + appid;

        stringRedisTemplate.delete(cacheKey);

        channelHandlerContext.fireChannelRead(imMessage);

    }

}
