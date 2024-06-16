package org.example.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.example.dao.pojo.TUserPhone;
import org.example.dto.UserLoginDTO;
import org.example.dto.TUserPhoneDTO;

import java.util.List;

/**
 * @author sz
 * @description 针对表【t_user_phone_00】的数据库操作Service
 * @createDate 2024-06-16 10:02:42
 */
public interface TUserPhoneService extends IService<TUserPhone> {


    UserLoginDTO login(String phone);


    List<TUserPhoneDTO> queryByUserId(Long userId);


    TUserPhoneDTO queryByPhone(String phone);

    boolean insertTUserPhone(TUserPhoneDTO tUserPhoneDTO);
}
