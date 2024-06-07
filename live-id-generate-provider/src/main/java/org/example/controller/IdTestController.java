package org.example.controller;

import jakarta.annotation.Resource;
import org.example.service.IdGenerateService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IdTestController {

    @Resource
    private IdGenerateService idGenerateService;

    @RequestMapping("/getId")
    public Integer getId(){
        return idGenerateService.getSeqId(1);
    }

}
