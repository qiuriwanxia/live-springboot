package org.example.interfaces;

import org.example.dto.UserLoginDTO;
import org.example.dto.TUserPhoneDTO;

import java.util.List;

public interface IUserPhoneRpc {


    /**
     * 根据手机号登录
     * @param phone
     * @return
     */
    UserLoginDTO login(String phone);


    /**
     * 根据用户id查询用户手机号信息
     * @param userId
     * @return
     */
    List<TUserPhoneDTO> queryByUserId(Long userId);


    /**
     * 根据手机号查询用户手机号信息
     * @param phone
     * @return
     */
    TUserPhoneDTO queryByPhone(String phone);
}
