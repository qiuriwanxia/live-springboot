package org.example.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.interfaces.IAccountTokenRPC;
import org.example.service.IAccountTokenService;

@DubboService
public class IAccountTokenRPCImpl implements IAccountTokenRPC {

    @Resource
    private IAccountTokenService iAccountTokenService;

    @Override
    public String createAndSaveLoginToken(Long userId) {
        return iAccountTokenService.createAndSaveLoginToken(userId);
    }

    @Override
    public Long getUserIdByToken(String tokenKey) {
        return iAccountTokenService.getUserIdByToken(tokenKey);
    }
}
