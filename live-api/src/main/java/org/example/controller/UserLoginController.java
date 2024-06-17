package org.example.controller;


import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.UserLoginDTO;
import org.example.service.UserLoginService;
import org.example.vo.UserLoginVo;
import org.example.vo.WebResponseVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userLogin")
public class UserLoginController {

    @Resource
    private UserLoginService userLoginService;

    @RequestMapping("/login")
    public WebResponseVO login(String phone, String code, HttpServletResponse response){
        WebResponseVO webResponseVO = userLoginService.login(phone,code);
        if (200==webResponseVO.getCode()) {
            UserLoginVo userLoginVo = (UserLoginVo) webResponseVO.getData();
            Cookie cookie = new Cookie("tk", userLoginVo.getToken());
            cookie.setDomain("qiyu.live.com");
            cookie.setPath("/");
            cookie.setMaxAge(30 * 24 * 3600);
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.addCookie(cookie);
        }
        return webResponseVO;
    }

    @RequestMapping("/sendLoginCode")
    public WebResponseVO sendLoginCode(String phone){
        return userLoginService.sendLoginCode(phone);
    }
}
