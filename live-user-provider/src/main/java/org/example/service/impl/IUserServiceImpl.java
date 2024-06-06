package org.example.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import org.example.dao.mapper.IUserMapper;
import org.example.dao.pojo.UserPO;
import org.example.dto.UserDTO;
import org.example.key.UserProviderCacheKey;
import org.example.rabbitmq.RabbitMQConfig;
import org.example.service.IUserService;
import org.example.util.ConvertBeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class IUserServiceImpl extends ServiceImpl<IUserMapper, UserPO> implements IUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IUserServiceImpl.class);

    @Resource
    private IUserMapper userMapper;

    @Resource
    private RedisTemplate<String, UserDTO> redisTemplate;

    @Resource
    private UserProviderCacheKey userProviderCacheKey;

    @Resource
    private RabbitTemplate rabbitTemplate;

    private UserPO convertToUserPo(UserDTO userDTO) {
        return ConvertBeanUtils.convert(userDTO, UserPO.class);
    }

    private List<UserPO> convertToUserPoList(List<UserDTO> userDTOList) {
        return ConvertBeanUtils.convertList(userDTOList, UserPO.class);
    }

    private List<UserDTO> convertToUserDTOList(List<UserPO> userPOList) {
        return ConvertBeanUtils.convertList(userPOList, UserDTO.class);
    }

    private UserDTO convertToUserDTO(UserPO userPO) {
        return ConvertBeanUtils.convert(userPO, UserDTO.class);
    }


    public UserDTO getByUserId(Long userId) {

        if (userId == null) {
            return null;
        }

        String key = userProviderCacheKey.buildUserInfoKey(userId.toString());

        UserDTO userDTO = redisTemplate.opsForValue().get(key);

        if (userDTO != null) {
            return userDTO;
        }

        UserPO userPO = userMapper.selectById(userId);

        UserDTO convertedToUserDTO = convertToUserDTO(userPO);

        redisTemplate.opsForValue().set(key, convertedToUserDTO,30, TimeUnit.SECONDS);

        return convertedToUserDTO;
    }

    @Override
    public boolean updateByUserId(UserDTO userDTO) {
        if (userDTO == null || userDTO.getUserId() == null) {
            return false;
        }
        boolean updateStatus = userMapper.updateById(convertToUserPo(userDTO)) > 0;
        if (updateStatus){
            rabbitTemplate.convertAndSend(RabbitMQConfig.COMMON_EXCHANGE,RabbitMQConfig.USER_UPDATE_QUEUE_KEY,new Message(JSON.toJSONBytes(userDTO)));
        }
        return updateStatus;
    }

    @Override
    public boolean insertUserId(UserDTO userDTO) {
        if (userDTO == null || userDTO.getUserId() == null) {
            return false;
        }
        return userMapper.insert(convertToUserPo(userDTO)) > 0;
    }

    @Override
    public Map<Long, UserDTO> batchQueryUserByIdList(List<Long> idList) {
        if (idList == null || idList.isEmpty()) {
            return Maps.newHashMap();
        }
        //先从缓存中拿
        List<String> keyList = idList.stream().map(id -> userProviderCacheKey.buildUserInfoKey(id.toString())).collect(Collectors.toList());

        //multiGet 方法有可能返回null
        List<UserDTO> userDTOList = redisTemplate.opsForValue().multiGet(keyList).stream().filter(userDTO -> userDTO != null).collect(Collectors.toList());

        if (userDTOList.size() == idList.size()) {
            return userDTOList.stream().collect(Collectors.toMap(UserDTO::getUserId, userDTO -> userDTO, (k1, k2) -> k1));
        }

        //如果少了，再查询
        List<Long> exitCacheIdList = userDTOList.stream().map(UserDTO::getUserId).collect(Collectors.toList());

        List<Long> needSerachIdList = idList.stream().filter(id -> !exitCacheIdList.contains(id)).collect(Collectors.toList());

        //多线程查询
        Map<Long, List<Long>> longListMap = needSerachIdList.stream().collect(Collectors.groupingBy(id -> id % 100));

        List<UserDTO> queryUserUserDTOList = new CopyOnWriteArrayList<>();

        longListMap.values().parallelStream().forEach(ids -> {
            List<UserPO> userPOS = userMapper.selectBatchIds(ids);
            List<UserDTO> userDTOList1 = convertToUserDTOList(userPOS);
            queryUserUserDTOList.addAll(userDTOList1);
        });

        if (!queryUserUserDTOList.isEmpty()) {

            userDTOList.addAll(queryUserUserDTOList);

            Map<String, UserDTO> keyUserDTOMap = userDTOList.stream().collect(Collectors.toMap((UserDTO d) -> {
                return userProviderCacheKey.buildUserInfoKey(d.getUserId().toString());
            }, userDTO -> userDTO, (k1, k2) -> k1));

            redisTemplate.opsForValue().multiSet(keyUserDTOMap);

            //管道批量处理
            redisTemplate.executePipelined(new SessionCallback<Object>() {
                @Override
                public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                    for (String s : keyUserDTOMap.keySet()) {
                        operations.expire((K) s,30,TimeUnit.SECONDS);

                    }
                    return null;
                }
            });
        }

        return userDTOList.stream().collect(Collectors.toMap(UserDTO::getUserId, userDTO -> userDTO, (k1, k2) -> k1));
    }

}
