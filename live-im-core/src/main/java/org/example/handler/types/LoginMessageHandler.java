package org.example.handler.types;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.shaded.com.google.common.base.Strings;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.example.cache.ChannelHandlerContextCache;
import org.example.config.ImAttributeKey;
import org.example.dto.ImMessageBody;
import org.example.handler.ImMessageStrategy;
import org.example.interfaces.ImTokenRpc;
import org.example.message.ImMessage;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoginMessageHandler implements ImMessageStrategy {


    @DubboReference
    private ImTokenRpc imTokenRpc;

    @Override
    public void handleMessage(ChannelHandlerContext channelHandlerContext, ImMessage imMessage) {
        log.info("开始处理登录消息");

        byte[] body = imMessage.getBody();

        if (body==null||body.length==0){
            channelHandlerContext.close();
        }

        ImMessageBody imMessageBody = JSON.parseObject(new String(body), ImMessageBody.class);


        //消息处理逻辑
        String token = imMessageBody.getToken();

        if (Strings.isNullOrEmpty(token)){
            //没有token不能建立连接
            log.error("token empty,imMessage is ",imMessage);
            throw  new IllegalArgumentException("token empty");
        }

        //如果token不为空，通过token获取userId
        Long userId = imTokenRpc.getUserId(token);

        if (userId==null){
            log.error("userId empty,imMessage is ",imMessage);
            throw  new IllegalArgumentException("userId empty");
        }

        //获取到了token,封装userId
        imMessageBody.setUserId(userId);
        imMessageBody.setData("true");
        imMessage.setBody(JSON.toJSONBytes(imMessageBody));

        channelHandlerContext.attr(ImAttributeKey.USER_ID_ATTR).set(userId);

        ChannelHandlerContextCache.add(userId,channelHandlerContext);

        channelHandlerContext.fireChannelRead(imMessage);

    }

}
