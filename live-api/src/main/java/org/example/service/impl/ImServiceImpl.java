package org.example.service.impl;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.dto.ImServerConfigDTO;
import org.example.enums.ImMessageAppEnum;
import org.example.rpc.interfaces.ImTokenRpc;
import org.example.rpc.interfaces.ImWsConfigRpc;
import org.example.service.ImService;
import org.example.util.ConvertBeanUtils;
import org.example.utils.LiveRequestUtil;
import org.example.vo.ImServerConfigVo;
import org.springframework.stereotype.Service;

@Service
public class ImServiceImpl implements ImService {

    @DubboReference
    private ImWsConfigRpc imWsConfigRpc;

    @DubboReference
    private ImTokenRpc imTokenRpc;


    @Override
    public ImServerConfigVo getImConfigUrl() {
        Long userId = LiveRequestUtil.getUserId();
        String token = imTokenRpc.createToken(userId, ImMessageAppEnum.LIVE_APP_BIZ.getCode());
        ImServerConfigDTO imServerConfigDTO = imWsConfigRpc.getImServerConfig();
        ImServerConfigVo imServerConfigVo = ConvertBeanUtils.convert(imServerConfigDTO, ImServerConfigVo.class);
        imServerConfigVo.setToken(token);
        return imServerConfigVo;
    }
}
