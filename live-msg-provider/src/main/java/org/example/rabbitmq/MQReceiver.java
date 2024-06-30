package org.example.rabbitmq;


import com.alibaba.fastjson2.JSON;
import com.rabbitmq.client.Channel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ImMessageBody;
import org.example.key.LiveMsgCacheKey;
import org.example.rabbit.RabbitMQConfig;
import org.example.service.SingleMessageHanlder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
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

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private LiveMsgCacheKey liveMsgCacheKey;

    //方法：接收消息
    @RabbitListener(queues = RabbitMQConfig.IM_BIZ_QUEUE, ackMode = "MANUAL")
    public void receive(Object msg, Channel channel) {
        long deliveryTag = ((Message) msg).getMessageProperties().getDeliveryTag();

        Message message = (Message) msg;
        String str = new String(message.getBody());

        ImMessageBody imMessageBody = JSON.parseObject(str, ImMessageBody.class);
        Long userId = imMessageBody.getUserId();

        //判断当前消息是否重试超出限制
        Object redisValue = redisTemplate.opsForHash().get(liveMsgCacheKey.buildMsgRetriesCodeMapKey(), String.valueOf(deliveryTag));
        Integer deliveryTimes = null;
        if (redisValue != null) {
            deliveryTimes = ((Number) redisValue).intValue();
            if (deliveryTimes > imMessageBody.getRetriesNub()) {
                //丢到死信队列中
                try {
                    channel.basicReject(deliveryTag, false);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
        }


        boolean sendStatus = singleMessageHanlder.onMessageReceiver(userId, imMessageBody);

        if (sendStatus) {
            try {
                channel.basicAck(deliveryTag, true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        redisTemplate.opsForHash().put(liveMsgCacheKey.buildMsgRetriesCodeMapKey(), String.valueOf(deliveryTag), redisValue == null ? "1" : String.valueOf(deliveryTimes + 1));

        rSendMessage(channel, message);


    }

    private static void rSendMessage(Channel channel, Message message) {
        MessageProperties messageProperties = message.getMessageProperties();
        try {
            channel.basicPublish(messageProperties.getReceivedExchange(), messageProperties.getReceivedRoutingKey(), null, message.getBody());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @RabbitListener(queues = RabbitMQConfig.IM_BIZ_DIE_QUEUE)
    public void receiveDieMsg(Object msg, Channel channel) {
        long deliveryTag = ((Message) msg).getMessageProperties().getDeliveryTag();

        Message message = (Message) msg;
        String str = new String(message.getBody());

        ImMessageBody imMessageBody = JSON.parseObject(str, ImMessageBody.class);

        log.info("接收到死信消息{}", JSON.toJSONString(imMessageBody));
    }

}
