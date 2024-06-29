package org.example.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.rpc.interfaces.ImOnlineRpc;
import org.example.service.ImOnlineService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class ImOnlineRpcImple implements ImOnlineRpc {

    @Resource
    private ImOnlineService imOnlineService;

    @Override
    public boolean isOnine(Long userId) {
        return imOnlineService.isOnine(userId);
    }

}
