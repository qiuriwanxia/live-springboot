package org.example.handler.types;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.example.handler.ImMessageStrategy;
import org.example.message.ImMessage;

@Slf4j
public class HeartBeatMessageHandler implements ImMessageStrategy {

    @Override
    public void handleMessage(ChannelHandlerContext channelHandlerContext, ImMessage imMessage) {
        System.out.println("开始处理心跳消息");
        channelHandlerContext.fireChannelRead(imMessage);

    }

}
