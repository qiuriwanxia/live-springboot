package org.example.handler;

import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import org.example.enums.ImMessageEnum;
import org.example.handler.types.BizMessageHandler;
import org.example.handler.types.HeartBeatMessageHandler;
import org.example.handler.types.LoginMessageHandler;
import org.example.handler.types.LoginOutMessageHandler;
import org.example.message.ImMessage;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ImMessageHandlerContext implements InitializingBean {


    @Resource
    private ApplicationContext app;

    static Map<Integer,ImMessageStrategy> messageStrategyMap = new HashMap<>();


    public static void doChannelRead(ChannelHandlerContext channelHandlerContext, ImMessage imMessage) {
        //真正处理消息
        int code = imMessage.getCode();

        ImMessageStrategy imMessageStrategy = messageStrategyMap.get(code);

        if (imMessageStrategy!=null) {
            imMessageStrategy.handleMessage(channelHandlerContext, imMessage);
        }

    }


    @Override
    public void afterPropertiesSet() throws Exception {
        messageStrategyMap.put(ImMessageEnum.LOGIN_MESSAGE.getCode(), app.getBean(LoginMessageHandler.class));
        messageStrategyMap.put(ImMessageEnum.LOGIN_OUT_MESSAGE.getCode(), app.getBean(LoginOutMessageHandler.class));
        messageStrategyMap.put(ImMessageEnum.BIZ_MESSAGE.getCode(), app.getBean(BizMessageHandler.class));
        messageStrategyMap.put(ImMessageEnum.HEART_BEAT_MESSAGE.getCode(),app.getBean(HeartBeatMessageHandler.class));
    }
}
