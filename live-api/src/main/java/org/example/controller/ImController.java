package org.example.controller;

import org.example.service.ImService;
import org.example.vo.ImServerConfigVo;
import org.example.vo.WebResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/im")
public class ImController {

    @Autowired
    private ImService imService;

    @PostMapping("/getImConfig")
    public WebResponseVO getImConfigUrl(){
        ImServerConfigVo imServerConfigVo = imService.getImConfigUrl();
        return WebResponseVO.success(imServerConfigVo);
    }

}
