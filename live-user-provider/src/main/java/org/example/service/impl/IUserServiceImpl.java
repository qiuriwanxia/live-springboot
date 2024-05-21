package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.dao.mapper.IUserMapper;
import org.example.dao.pojo.UserPO;
import org.example.dto.UserDTO;
import org.example.service.IUserService;
import org.example.util.ConvertBeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class IUserServiceImpl extends ServiceImpl<IUserMapper, UserPO> implements IUserService {

    @Resource
    private IUserMapper userMapper;

    @Resource
    private RedisTemplate<String,UserDTO> redisTemplate;

    private UserPO convertToUserPo(UserDTO userDTO) {
        return ConvertBeanUtils.convert(userDTO, UserPO.class);
    }

    private UserDTO convertToUserDTO(UserPO userPO) {
        return ConvertBeanUtils.convert(userPO, UserDTO.class);
    }

    private static final String REDIS_KEY_PREFIX ="userinfo";

    public UserDTO getByUserId(Long userId) {

        if (userId == null) {
            return null;
        }

        String key = REDIS_KEY_PREFIX.concat(userId.toString());

        UserDTO userDTO = redisTemplate.opsForValue().get(key);

        if (userDTO!=null){
            return userDTO;
        }

        UserPO userPO = userMapper.selectById(userId);

        UserDTO convertedToUserDTO = convertToUserDTO(userPO);

        redisTemplate.opsForValue().set(key,convertedToUserDTO);

        return convertedToUserDTO;
    }

    @Override
    public boolean updateByUserId(UserDTO userDTO) {
        if (userDTO == null || userDTO.getUserId() == null) {
            return false;
        }
        return userMapper.updateById(convertToUserPo(userDTO)) > 0;
    }

    @Override
    public boolean insertUserId(UserDTO userDTO) {
        if (userDTO == null || userDTO.getUserId() == null) {
            return false;
        }
        return userMapper.insert(convertToUserPo(userDTO)) > 0;
    }

}
