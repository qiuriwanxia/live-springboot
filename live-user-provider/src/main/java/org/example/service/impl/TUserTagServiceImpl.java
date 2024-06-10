package org.example.service.impl;


import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.bo.ConvertBeanBase;
import org.example.constant.CacheAsyncDeleteCode;
import org.example.constant.UserTagsEnum;
import org.example.dao.mapper.TUserTagMapper;
import org.example.dao.pojo.TUserTag;
import org.example.dto.CacheAsyncDeleteDTO;
import org.example.dto.TUserTagDTO;
import org.example.key.UserProviderCacheKey;
import org.example.rabbitmq.RabbitMQConfig;
import org.example.service.TUserTagService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author sz
 * @description 针对表【t_user_tag(用户标签记录)】的数据库操作Service实现
 * @createDate 2024-06-07 20:15:13
 */
@Slf4j
@Service
public class TUserTagServiceImpl extends ServiceImpl<TUserTagMapper, TUserTag>
        implements TUserTagService {


    ConvertBeanBase convertBeanBase = new ConvertBeanBase<TUserTag, TUserTagDTO>(TUserTag.class,TUserTagDTO.class);

    @Resource
    private TUserTagMapper tUserTagMapper;

    private List<Field> tUserTagField = Arrays.asList(TUserTag.class.getDeclaredFields());

    private Class tUserTagClass = TUserTag.class;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private RedisTemplate<String, Object> tUserTagDTORedisTemplate;

    @Resource
    private UserProviderCacheKey userProviderCacheKey;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public boolean setTag(Long userId, UserTagsEnum userTagsEnum) {
        if (!baseVaild(userId, userTagsEnum)) return false;

        boolean setTagBoolean = tUserTagMapper.updateTag(userId, userTagsEnum) > 0;

        TUserTag tUserTag = gettUserTagForRedis(userId, userProviderCacheKey.buildUserTagKey(userId));

        if (!setTagBoolean) {

            String execute = redisTemplate.execute(new RedisCallback<String>() {
                @Override
                public String doInRedis(RedisConnection connection) throws DataAccessException {
                    RedisSerializer keySerializer = redisTemplate.getKeySerializer();
                    RedisSerializer valueSerializer = redisTemplate.getValueSerializer();

                    return (String) connection.execute("set",
                            keySerializer.serialize(userProviderCacheKey.buildUserTagLockKey(userId))
                            , valueSerializer.serialize("-1"),
                            "NX".getBytes(StandardCharsets.UTF_8),
                            "EX".getBytes(StandardCharsets.UTF_8),
                            "3".getBytes(StandardCharsets.UTF_8)
                    );

                }
            });

            if (!"OK".equals(execute)) return false;

            //更新失败，判断用户标签之前是否存在
            if (tUserTag == null) {
                //插入数据
                tUserTag = new TUserTag();
                tUserTag.setUserId(userId);
                tUserTag.setCreateTime(new Date());

                tUserTagMapper.insert(tUserTag);


                setTagBoolean = tUserTagMapper.updateTag(userId, userTagsEnum) > 0;

            }
            ;
        }

        if (setTagBoolean){
            //删除缓存 发送消息
            rabbitTemplate.convertAndSend(RabbitMQConfig.COMMON_EXCHANGE, RabbitMQConfig.USER_UPDATE_QUEUE_KEY,
                    new Message(CacheAsyncDeleteDTO.buildThenTobytes(
                            CacheAsyncDeleteCode.USER_TAG_DELETE.getCode(),JSON.toJSONString(convertBeanBase.convertToV(tUserTag))))
            );
        }

        return setTagBoolean;
    }


    @Override
    public boolean cancelTag(Long userId, UserTagsEnum userTagsEnum) {
        if (!baseVaild(userId, userTagsEnum)) return false;

        boolean canceltagBoolean = tUserTagMapper.cancelTag(userId, userTagsEnum) > 0;


        if (canceltagBoolean){
            //删除缓存 发送消息
            TUserTag tUserTag = new TUserTag();
            tUserTag.setUserId(userId);

            rabbitTemplate.convertAndSend(RabbitMQConfig.COMMON_EXCHANGE, RabbitMQConfig.USER_UPDATE_QUEUE_KEY,
                    new Message(CacheAsyncDeleteDTO.buildThenTobytes(
                            CacheAsyncDeleteCode.USER_TAG_DELETE.getCode(),JSON.toJSONString(convertBeanBase.convertToV(tUserTag)))));
        }

        return canceltagBoolean;
    }

    @Override
    public boolean containTag(Long userId, UserTagsEnum userTagsEnum) {
        if (!baseVaild(userId, userTagsEnum)) return false;

        Long tagValue = getFieldValue(userId, userTagsEnum);

        log.info("tagValue = " + tagValue);

        long tag = userTagsEnum.getTag();
        return (tagValue & tag) == tag;
    }

    private Long getFieldValue(Long userId, UserTagsEnum userTagsEnum) {
        String key = userProviderCacheKey.buildUserTagKey(userId);

        TUserTag tUserTag = gettUserTagForRedis(userId, key);

        Field declaredField;
        try {
            declaredField = tUserTagClass.getDeclaredField(userTagsEnum.getFiledName());
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        declaredField.setAccessible(true);
        Long tagValue;
        try {
            tagValue = (Long) declaredField.get(tUserTag);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return tagValue;
    }

    private TUserTag gettUserTagForRedis(Long userId, String key) {
        TUserTagDTO tUserTagDTO = (TUserTagDTO) tUserTagDTORedisTemplate.opsForValue().get(key);

        TUserTag tUserTag;

        if (tUserTagDTO == null) {

            tUserTag = tUserTagMapper.selectByUserId(userId);

            //缓存
            if (tUserTag!=null) {
                tUserTagDTORedisTemplate.opsForValue().set(key, (TUserTagDTO) convertBeanBase.convertToV(tUserTag), 30, TimeUnit.SECONDS);
            }

        } else {
            tUserTag = (TUserTag) convertBeanBase.convertToK(tUserTagDTO);
        }
        return tUserTag;
    }

    private boolean baseVaild(Long userId, UserTagsEnum userTagsEnum) {
        if (userId == null) {
            return true;
        }
        String filedName = userTagsEnum.getFiledName();
        if (!tUserTagField.contains(filedName)) {
            return true;
        }

        return false;
    }
}




