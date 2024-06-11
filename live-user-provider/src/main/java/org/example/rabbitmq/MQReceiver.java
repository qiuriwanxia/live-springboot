package org.example.rabbitmq;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.constant.CacheAsyncDeleteCode;
import org.example.dto.CacheAsyncDeleteDTO;
import org.example.dto.TUserTagDTO;
import org.example.dto.UserDTO;
import org.example.key.UserProviderCacheKey;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

import static org.example.constant.CacheAsyncDeleteCode.USER_TAG_DELETE;

/**
 * 消息接收者
 */
@Slf4j
@Service
public class MQReceiver {

    @Resource
    private UserProviderCacheKey userProviderCacheKey;

    @Resource
    private RedisTemplate<String, UserDTO> redisTemplate;

    //方法：接收消息
    @RabbitListener(queues = RabbitMQConfig.USER_UPDATE_TTL_QUEUE)
    public void receive(Object msg) {

        Message message = (Message) msg;
        String str = new String(message.getBody());
        if (!str.isEmpty()) {
            CacheAsyncDeleteDTO cacheAsyncDeleteDTO = JSON.parseObject(str, CacheAsyncDeleteDTO.class);
            log.info("接收到消息:" + str);
            int code = cacheAsyncDeleteDTO.getCode();
            String json = cacheAsyncDeleteDTO.getJson();
            String key;
            if (USER_TAG_DELETE.code == code) {
                TUserTagDTO tUserTagDTO = JSON.parseObject(json, TUserTagDTO.class);
                log.info("tUserTagDTO =  {}", JSON.toJSONString(tUserTagDTO));
                key = userProviderCacheKey.buildUserTagKey(tUserTagDTO.getUserId());
            } else {
                UserDTO userDTO = JSON.parseObject(json, UserDTO.class);
                log.info("userDTO =  {}", JSON.toJSONString(userDTO));
                key = userProviderCacheKey.buildUserInfoKey(userDTO.getUserId().toString());
            }

            //删除缓存
            Boolean cacheDel = redisTemplate.delete(key);

            log.info("key {} 缓存删除 {}",key,cacheDel);
        }

    }

}
