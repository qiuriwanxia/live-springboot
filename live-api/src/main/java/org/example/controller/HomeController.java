package org.example.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.example.constant.GateWayHeadEnum;
import org.example.service.HomePageService;
import org.example.vo.HomePageVo;
import org.example.vo.WebResponseVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class HomeController {

    @Resource
    private HttpServletRequest request;

    @Resource
    private HomePageService homePageService;

    @RequestMapping("/initPage")
    public WebResponseVO inittest() {
        Long userId = Long.valueOf(request.getHeader(GateWayHeadEnum.USER_LOGIN_ID.getName()));
        HomePageVo homePageVo = homePageService.initPage(userId);
        return WebResponseVO.success(homePageVo);
    }

}
