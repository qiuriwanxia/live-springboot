package org.example.service.impl;

import org.apache.dubbo.config.annotation.DubboReference;
import org.example.bo.ConvertBeanBase;
import org.example.constant.MsgSendResultEnum;
import org.example.dto.UserDTO;
import org.example.dto.UserLoginDTO;
import org.example.interfaces.IUserPhoneRpc;
import org.example.interfaces.MsgRpc;
import org.example.service.UserLoginService;
import org.example.vo.UserLoginVo;
import org.example.vo.WebResponseVO;
import org.springframework.stereotype.Service;

@Service
public class UserLoginServiceImpl implements UserLoginService {

    @DubboReference
    private IUserPhoneRpc userPhoneRpc;

    @DubboReference
    private MsgRpc msgRpc;

    ConvertBeanBase convertBeanBase = new ConvertBeanBase<UserLoginDTO, UserLoginVo>(UserLoginDTO.class, UserLoginVo.class);


    @Override
    public WebResponseVO login(String phone, String code) {
        MsgSendResultEnum msgSendResultEnum = msgRpc.checkMsgCode(phone, code);
        if (MsgSendResultEnum.CHECK_SUCCESS.getCode()==msgSendResultEnum.getCode()){
            //短信验证码校验成功，执行登录操作
            UserLoginDTO userLoginDTO = userPhoneRpc.login(phone);
            UserLoginVo userLoginVo = (UserLoginVo) convertBeanBase.convertToV(userLoginDTO);
            return WebResponseVO.success(userLoginVo);
        }
        return WebResponseVO.error(msgSendResultEnum.getDesc());
    }

    @Override
    public WebResponseVO sendLoginCode(String phone) {
        MsgSendResultEnum msgSendResultEnum = msgRpc.sendMsg(phone);
        if (MsgSendResultEnum.SEND_SUCCESS.getCode()==msgSendResultEnum.getCode()){
            return WebResponseVO.success(msgSendResultEnum);
        }
        return WebResponseVO.error(msgSendResultEnum.getDesc());
    }
}
