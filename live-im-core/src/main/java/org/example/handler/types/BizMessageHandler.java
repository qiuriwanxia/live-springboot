package org.example.handler.types;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.example.handler.ImMessageStrategy;
import org.example.message.ImMessage;

@Slf4j
public class BizMessageHandler implements ImMessageStrategy {
    @Override
    public void handleMessage(ChannelHandlerContext channelHandlerContext, ImMessage imMessage) {
        log.info("开始处理业务消息");
        channelHandlerContext.fireChannelRead(imMessage);
    }
}
