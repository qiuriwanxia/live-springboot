package org.example.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.rpc.interfaces.ImTokenRpc;
import org.example.service.ImTokenService;

@DubboService
public class ImTokenRpcImpl implements ImTokenRpc {

    @Resource
    private ImTokenService imTokenService;

    @Override
    public String createToken(Long userId,String appid) {
        return imTokenService.createToken(userId,appid);
    }

    @Override
    public Long getUserId(String token) {
        return imTokenService.getUserId(token);
    }
}
