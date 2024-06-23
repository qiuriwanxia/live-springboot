package org.example.handler;

import io.netty.channel.ChannelHandlerContext;
import org.example.message.ImMessage;

public interface ImMessageStrategy {

    public void handleMessage(ChannelHandlerContext channelHandlerContext, ImMessage imMessage);

}
