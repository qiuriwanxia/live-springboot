package org.example.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.dto.ImServerConfigDTO;
import org.example.rpc.interfaces.ImWsConfigRpc;
import org.example.service.ImWsConfigService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class ImWsConfigRpcImpl implements ImWsConfigRpc {

    @Resource
    private ImWsConfigService imWsConfigService;

    @Override
    public ImServerConfigDTO getImServerConfig() {
        return imWsConfigService.getImServerConfig();
    }
}
