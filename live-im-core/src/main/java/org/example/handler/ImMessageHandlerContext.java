package org.example.handler;

import io.netty.channel.ChannelHandlerContext;
import org.example.enums.ImMessageEnum;
import org.example.handler.types.BizMessageHandler;
import org.example.handler.types.HeartBeatMessageHandler;
import org.example.handler.types.LoginMessageHandler;
import org.example.handler.types.LoginOutMessageHandler;
import org.example.message.ImMessage;

import java.util.HashMap;
import java.util.Map;

public class ImMessageHandlerContext {

    static Map<Integer,ImMessageStrategy> messageStrategyMap = new HashMap<>();

    static {
        messageStrategyMap.put(ImMessageEnum.LOGIN_MESSAGE.getCode(), new LoginMessageHandler());
        messageStrategyMap.put(ImMessageEnum.LOGIN_OUT_MESSAGE.getCode(), new LoginOutMessageHandler());
        messageStrategyMap.put(ImMessageEnum.BIZ_MESSAGE.getCode(), new BizMessageHandler());
        messageStrategyMap.put(ImMessageEnum.HEART_BEAT_MESSAGE.getCode(), new HeartBeatMessageHandler());
    }

    public static void doChannelRead(ChannelHandlerContext channelHandlerContext, ImMessage imMessage) {
        //真正处理消息
        int code = imMessage.getCode();

        ImMessageStrategy imMessageStrategy = messageStrategyMap.get(code);

        if (imMessageStrategy!=null) {
            imMessageStrategy.handleMessage(channelHandlerContext, imMessage);
        }

    }


}
