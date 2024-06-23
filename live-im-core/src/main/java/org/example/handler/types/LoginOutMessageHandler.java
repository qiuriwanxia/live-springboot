package org.example.handler.types;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.cache.ChannelHandlerContextCache;
import org.example.dto.ImMessageBody;
import org.example.handler.ImMessageStrategy;
import org.example.interfaces.ImTokenRpc;
import org.example.message.ImMessage;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoginOutMessageHandler implements ImMessageStrategy {

    @Resource
    private ImTokenRpc imTokenRpc;

    @Override
    public void handleMessage(ChannelHandlerContext channelHandlerContext, ImMessage imMessage) {
        log.info("开始处理登出消息");

        byte[] body = imMessage.getBody();

        if (body==null||body.length==0){
            channelHandlerContext.close();
        }

        ImMessageBody imMessageBody = JSON.parseObject(new String(body), ImMessageBody.class);


        Long userId = imMessageBody.getUserId();
        String token = imMessageBody.getToken();

        if (userId==null){
            userId = imTokenRpc.getUserId(token);
        }

        //删除缓存
        ChannelHandlerContextCache.remove(userId);

        channelHandlerContext.fireChannelRead(imMessage);

    }

}
