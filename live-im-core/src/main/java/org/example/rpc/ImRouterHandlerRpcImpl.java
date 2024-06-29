package org.example.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.dto.ImMessageBody;
import org.example.rpc.interfaces.ImRouterHandlerRpc;
import org.example.service.ImRouterHandlerService;

@DubboService
public class ImRouterHandlerRpcImpl implements ImRouterHandlerRpc {

    @Resource
    private ImRouterHandlerService imRouterHandlerService;

    @Override
    public void sendMsg(Long rUserId, ImMessageBody messageBody) {
        imRouterHandlerService.onReceive(rUserId,messageBody);
    }
}
