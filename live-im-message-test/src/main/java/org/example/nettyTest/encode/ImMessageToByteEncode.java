package org.example.nettyTest.encode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.example.message.ImMessage;

public class ImMessageToByteEncode extends MessageToByteEncoder {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        ImMessage imMessage = (ImMessage) o;
        byteBuf.writeShort(imMessage.getMagic());
        byteBuf.writeInt(imMessage.getCode());
        byteBuf.writeInt(imMessage.getLength());
        byteBuf.writeBytes(imMessage.getBody());
        channelHandlerContext.writeAndFlush(imMessage);
    }

}
