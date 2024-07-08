package org.example.ws;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.cache.ChannelHandlerContextCache;
import org.example.config.ImAttributeKey;
import org.example.constant.ImMessageConstans;
import org.example.handler.ImMessageHandlerContext;
import org.example.message.ImMessage;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ChannelHandler.Sharable
public class WsImServerCoreHandler extends SimpleChannelInboundHandler {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        log.info("接收到客户端消息");
        if (o instanceof WebSocketFrame){
            wsMsgHanlder(channelHandlerContext,(WebSocketFrame)o);
        }
    }

    private void wsMsgHanlder(ChannelHandlerContext channelHandlerContext, WebSocketFrame o) {
        //如果不是文本消息，统一后台不会处理
        if(!(o instanceof TextWebSocketFrame)){
            log.error("msg type not supported!");
            return;
        }
        try {
            //返回应答消息
            String content = ((TextWebSocketFrame)o).text();
            JSONObject jsonObject = JSON.parseObject(content, JSONObject.class);
            ImMessage imMessage = new ImMessage();
            imMessage.setMagic(jsonObject.getShort("magic"));
            imMessage.setCode(jsonObject.getInteger("code"));
            imMessage.setLength(jsonObject.getShort("length"));
            imMessage.setBody(JSON.toJSONBytes(jsonObject.getString("body")));
            ImMessageHandlerContext.doChannelRead(channelHandlerContext,imMessage);
        }catch (Exception e){
            log.error("ws handler error is : "+e);
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
