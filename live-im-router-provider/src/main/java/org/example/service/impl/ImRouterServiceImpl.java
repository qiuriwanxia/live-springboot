package org.example.service.impl;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.RpcContext;
import org.assertj.core.util.Strings;
import org.example.constant.ImMessageConstans;
import org.example.dto.ImMessageBody;
import org.example.rpc.interfaces.ImRouterHandlerRpc;
import org.example.service.ImRouterService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ImRouterServiceImpl implements ImRouterService {

    @DubboReference
    private ImRouterHandlerRpc imRouterHandlerRpc;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public boolean sendMsg(Long rUserId, ImMessageBody messageBody) {

        String appid = messageBody.getAppid();
        String cacheKey = ImMessageConstans.USER_BIND_IP_CACHE_KEY + rUserId + ":" + appid;
        String ip = stringRedisTemplate.opsForValue().get(cacheKey);

        if (Strings.isNullOrEmpty(ip)){
            //用户不在线，无法发送消息
            return false;
        }

        RpcContext.getContext().set("ip",ip);

        imRouterHandlerRpc.sendMsg(rUserId,messageBody);

        return true;
    }
}
