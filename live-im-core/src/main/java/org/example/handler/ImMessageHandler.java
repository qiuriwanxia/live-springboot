package org.example.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.cache.ChannelHandlerContextCache;
import org.example.config.ImAttributeKey;
import org.example.constant.ImMessageConstans;
import org.example.message.ImMessage;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ChannelHandler.Sharable
public class ImMessageHandler extends SimpleChannelInboundHandler {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        log.info("接收到客户端消息");
        if (o instanceof ImMessage){
            ImMessageHandlerContext.doChannelRead(channelHandlerContext,(ImMessage)o);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Object value = ctx.attr(ImAttributeKey.USER_ID_ATTR).get();
        Object appid = ctx.attr(ImAttributeKey.APP_ID).get();
        if (value!=null){
            Long userId = (Long) value;
            ChannelHandlerContextCache.remove(userId);

            String cacheKey = ImMessageConstans.USER_BIND_IP_CACHE_KEY + userId + ":" + appid;
            stringRedisTemplate.delete(cacheKey);
        }


        super.channelInactive(ctx);
    }


}
