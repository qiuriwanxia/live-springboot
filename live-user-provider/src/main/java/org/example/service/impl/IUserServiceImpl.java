package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.dao.mapper.IUserMapper;
import org.example.dao.pojo.UserPO;
import org.example.dto.UserDTO;
import org.example.service.IUserService;
import org.example.util.ConvertBeanUtils;
import org.springframework.stereotype.Service;

@Service
public class IUserServiceImpl extends ServiceImpl<IUserMapper, UserPO> implements IUserService {

    @Resource
    private IUserMapper userMapper;

    private UserPO convertToUserPo(UserDTO userDTO) {
        return ConvertBeanUtils.convert(userDTO, UserPO.class);
    }

    private UserDTO convertToUserDTO(UserPO userPO) {
        return ConvertBeanUtils.convert(userPO, UserDTO.class);
    }

    public UserDTO getByUserId(Long userId) {

        if (userId == null) {
            return null;
        }

        UserPO userPO = userMapper.selectById(userId);

        return convertToUserDTO(userPO);
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
