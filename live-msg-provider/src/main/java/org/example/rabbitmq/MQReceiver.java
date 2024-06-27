package org.example.rabbitmq;


import com.alibaba.fastjson2.JSON;
import com.rabbitmq.client.Channel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ImMessageBody;
import org.example.rabbit.RabbitMQConfig;
import org.example.service.SingleMessageHanlder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;


/**
 * 消息接收者
 */
@Slf4j
@Service
public class MQReceiver {


    @Resource
    private SingleMessageHanlder singleMessageHanlder;

    //方法：接收消息
    @RabbitListener(queues = RabbitMQConfig.IM_BIZ_QUEUE)
    public void receive(Object msg, Channel channel) {
        Message message = (Message) msg;
        long deliveryTag = ((Message) msg).getMessageProperties().getDeliveryTag();
        try {
            channel.basicAck(deliveryTag,true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String str = new String(message.getBody());

        ImMessageBody imMessageBody = JSON.parseObject(str, ImMessageBody.class);
        Long userId = imMessageBody.getUserId();

        singleMessageHanlder.onMessageReceiver(userId,imMessageBody);

    }

}
