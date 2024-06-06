package org.example.rabbitmq;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import org.example.dto.UserDTO;
import org.example.key.UserProviderCacheKey;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * 消息接收者
 */
@Service
public class MQReceiver {

    @Resource
    private UserProviderCacheKey userProviderCacheKey;

    @Resource
    private RedisTemplate<String, UserDTO> redisTemplate;

    private Logger log = Logger.getLogger(String.valueOf(MQReceiver.class));

    //方法：接收消息
    @RabbitListener(queues = RabbitMQConfig.USER_UPDATE_TTL_QUEUE)
    public void receive(Object msg) {

        Message message = (Message) msg;
        String str =new String(message.getBody());
        if (!str.isEmpty()){
            UserDTO userDTO = JSON.parseObject(str, UserDTO.class);
            log.info("接收到消息:"+str);
            //删除缓存
            String key = userProviderCacheKey.buildUserInfoKey(userDTO.getUserId().toString());
            redisTemplate.delete(key);
        }

    }

}
