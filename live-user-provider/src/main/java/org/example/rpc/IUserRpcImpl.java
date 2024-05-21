package org.example.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.dto.UserDTO;
import org.example.interfaces.IUserRpc;
import org.example.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class IUserRpcImpl implements IUserRpc {

    @Resource
    private IUserService userService;

    @Override
    public String test() {
        System.out.println("this is dubbo test");
        return "success";
    }

    @Override
    public UserDTO getByUserId(Long userId) {
        return userService.getByUserId(userId);
    }

    @Override
    public boolean updateByUserId(UserDTO userDTO) {
        return userService.updateByUserId(userDTO);
    }

    @Override
    public boolean insertUserId(UserDTO userDTO) {
        return userService.insertUserId(userDTO);
    }
}
