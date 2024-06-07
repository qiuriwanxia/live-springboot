package org.example.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.service.IdGenerateService;

@DubboService
public class IdBuilderRpcImple
        implements org.example.interfaces.IdBuilderRpc {

    @Resource
    private IdGenerateService idGenerateService;

    @Override
    public Integer increaseSeqId(int code) {
        return idGenerateService.getSeqId(code);
    }

    @Override
    public Integer increaseUnSeqId(int code) {
        return idGenerateService.getUnSeqId(code);
    }

    @Override
    public String increaseSeqStrId(int code) {
        return null;
    }
}
