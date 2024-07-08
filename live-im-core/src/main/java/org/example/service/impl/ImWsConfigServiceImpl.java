package org.example.service.impl;

import org.apache.dubbo.config.annotation.DubboReference;
import org.example.dto.ImServerConfigDTO;
import org.example.rpc.interfaces.ImTokenRpc;
import org.example.service.ImWsConfigService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ImWsConfigServiceImpl implements ImWsConfigService {


    @Value("${im.ws.port}")
    private int port;

    @Value("${im.server.ip}")
    private String serverIp;

    @DubboReference
    private ImTokenRpc imTokenRpc;

    @Override
    public ImServerConfigDTO getImServerConfig() {
        ImServerConfigDTO imServerConfigDTO = new ImServerConfigDTO();
        imServerConfigDTO.setWsImServerAddress(serverIp+":"+port);
        imServerConfigDTO.setToken("");
        return imServerConfigDTO;
    }

}
