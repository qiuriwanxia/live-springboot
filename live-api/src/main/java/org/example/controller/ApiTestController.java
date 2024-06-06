package org.example.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.dubbo.config.annotation.DubboReference;
import org.example.dto.UserDTO;
import org.example.interfaces.IUserRpc;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class ApiTestController {

    @DubboReference
    private IUserRpc iUserRpc;


    @ApiOperation(value = "根据id获取用户信息")
    @GetMapping("/get/{id}")
    public UserDTO getByUserId(@PathVariable("id")Long id){
        return iUserRpc.getByUserId(id);
    }

    @ApiOperation(value = "id批量查询用户信息")
    @PostMapping("/batchQueryUserByIdList")
    public Map<Long,UserDTO> batchQueryUserByIdList(@RequestBody List<Long> idList){
        return iUserRpc.batchQueryUserByIdList(idList);
    }

    @ApiOperation(value = "根据id更新用户昵称")
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

    @ApiOperation(value = "添加单个用户")
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
