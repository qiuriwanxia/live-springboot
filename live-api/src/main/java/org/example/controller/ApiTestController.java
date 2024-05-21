package org.example.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.example.dto.UserDTO;
import org.example.interfaces.IUserRpc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class ApiTestController {

    @DubboReference
    private IUserRpc iUserRpc;


    @GetMapping("/get/{id}")
    public UserDTO getByUserId(@PathVariable("id")Long id){
        return iUserRpc.getByUserId(id);
    }

    @GetMapping("/update/{id}/{nickName}")
    public UserDTO update(@PathVariable("id")Long id,@PathVariable("nickName")String nickName){
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(id);
        userDTO.setNickName(nickName);
        if (iUserRpc.updateByUserId(userDTO)) {
            return userDTO;
        }else {
            return null;
        }
    }

    @GetMapping("/insert/{id}/{nickName}")
    public UserDTO insert(@PathVariable("id")Long id,@PathVariable("nickName")String nickName){
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(id);
        userDTO.setNickName(nickName);
        if (iUserRpc.insertUserId(userDTO)) {
            return userDTO;
        }else {
            return null;
        }
    }

}
