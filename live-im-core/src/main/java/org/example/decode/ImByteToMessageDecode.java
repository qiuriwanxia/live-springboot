package org.example.decode;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.example.constant.ImMessageMagic;
import org.example.message.ImMessage;

import java.util.List;

@Slf4j
public class ImByteToMessageDecode extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

        short magic = byteBuf.readShort();
        if (magic == ImMessageMagic.magic) {

            int code = byteBuf.readInt();

            int length = byteBuf.readInt();

            byte[] body = new byte[length];

            int readableBytes = byteBuf.readableBytes();

            for (int i = 0; i < readableBytes; i++) {
                byte readByte = byteBuf.readByte();
                body[i]=readByte;
            }

//            byteBuf.readBytes(byteBuf);

            ImMessage imMessage = new ImMessage();
            imMessage.setMagic(magic);
            imMessage.setCode(code);
            imMessage.setLength(length);
            imMessage.setBody(body);

            log.info("接收到消息 {}", JSON.toJSONString(imMessage));

            list.add(imMessage);
        }
    }
}
