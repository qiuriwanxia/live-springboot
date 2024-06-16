package org.example.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.dto.UserLoginDTO;
import org.example.dto.TUserPhoneDTO;
import org.example.interfaces.IUserPhoneRpc;
import org.example.service.TUserPhoneService;

import java.util.List;

@DubboService
public class IUserPhoneRpcImpl implements IUserPhoneRpc {

    @Resource
    private TUserPhoneService tUserPhoneService;


    @Override
    public UserLoginDTO login(String phone) {
        return tUserPhoneService.login(phone);
    }

    @Override
    public List<TUserPhoneDTO> queryByUserId(Long userId) {
        return tUserPhoneService.queryByUserId(userId);
    }

    @Override
    public TUserPhoneDTO queryByPhone(String phone) {
        return tUserPhoneService.queryByPhone(phone);
    }


}
