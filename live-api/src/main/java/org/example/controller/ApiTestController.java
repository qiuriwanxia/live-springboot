package org.example.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.example.interfaces.IUserRpc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class ApiTestController {

    @DubboReference
    private IUserRpc iUserRpc;


    @GetMapping("/dubbo")
    public String test(){
        return iUserRpc.test();
    }

}
