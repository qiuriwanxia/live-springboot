package org.example.service;

import org.example.vo.WebResponseVO;

public interface UserLoginService {
    WebResponseVO login(String phone, String code);

    WebResponseVO sendLoginCode(String phone);
}
