package org.example.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.example.message.ImMessage;

@Slf4j
public class ImMessageHandler extends SimpleChannelInboundHandler {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        log.info("接收到客户端消息");
        if (o instanceof ImMessage){
            ImMessageHandlerContext.doChannelRead(channelHandlerContext,(ImMessage)o);
        }
    }
}
