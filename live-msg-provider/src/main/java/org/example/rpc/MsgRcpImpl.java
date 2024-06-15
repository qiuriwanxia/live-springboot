package org.example.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.constant.MsgSendResultEnum;
import org.example.interfaces.MsgRpc;
import org.example.service.TSmsService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class MsgRcpImpl implements MsgRpc {

    @Resource
    private TSmsService tSmsService;

    @Override
    public MsgSendResultEnum sendMsg(String phone) {
        return null;
    }

    @Override
    public MsgSendResultEnum checkMsgCode(String phone, String code) {
        return null;
    }
}
