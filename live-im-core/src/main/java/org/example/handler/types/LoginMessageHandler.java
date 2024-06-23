package org.example.handler.types;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ImMessageBody;
import org.example.handler.ImMessageStrategy;
import org.example.message.ImMessage;

@Slf4j
public class LoginMessageHandler implements ImMessageStrategy {

    @Override
    public void handleMessage(ChannelHandlerContext channelHandlerContext, ImMessage imMessage) {
        log.info("开始处理登录消息");

        byte[] body = imMessage.getBody();

        ImMessageBody imMessageBody = JSON.parseObject(new String(body), ImMessageBody.class);

        //消息处理逻辑

        channelHandlerContext.fireChannelRead(imMessage);

    }

}
