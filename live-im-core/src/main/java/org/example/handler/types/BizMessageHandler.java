package org.example.handler.types;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.assertj.core.util.Strings;
import org.example.dto.ImMessageBody;
import org.example.handler.ImMessageStrategy;
import org.example.rpc.interfaces.ImTokenRpc;
import org.example.message.ImMessage;

import org.example.rabbit.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BizMessageHandler implements ImMessageStrategy {


    @Resource
    private RabbitTemplate rabbitTemplate;

    @DubboReference
    private ImTokenRpc imTokenRpc;

    @Override
    public void handleMessage(ChannelHandlerContext channelHandlerContext, ImMessage imMessage) {
        log.info("开始处理业务消息");

        byte[] body = imMessage.getBody();

        if (body.length==0){
            channelHandlerContext.close();
            return;
        }

        ImMessageBody imMessageBody = JSON.parseObject(new String(body), ImMessageBody.class);
        String token = imMessageBody.getToken();

        Long userId = imTokenRpc.getUserId(token);
        imMessageBody.setUserId(userId);

        String appid = imMessageBody.getAppid();

        if (userId==null|| Strings.isNullOrEmpty(appid)){
            log.error("userId empty,imMessage is ",imMessage);
            throw  new IllegalArgumentException("userId empty");
        }

        rabbitTemplate.convertAndSend(RabbitMQConfig.IM_EXCHANGE,RabbitMQConfig.IM_BIZ_KEY,JSON.toJSONBytes(imMessageBody));

        channelHandlerContext.fireChannelRead(imMessage);
    }
}
