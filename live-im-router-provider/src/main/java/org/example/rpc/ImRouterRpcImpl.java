package org.example.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.ImRouterRpc;
import org.example.dto.ImMessageBody;
import org.example.service.ImRouterService;

@DubboService
public class ImRouterRpcImpl implements ImRouterRpc {

    @Resource
    private ImRouterService imRouterService;

    @Override
    public boolean sendMsg(Long rUserId, ImMessageBody messageBody) {
       return imRouterService.sendMsg(rUserId,messageBody);
    }

}
