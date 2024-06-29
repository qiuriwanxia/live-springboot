package org.example.handler.types;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.shaded.com.google.common.base.Strings;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.example.cache.ChannelHandlerContextCache;
import org.example.config.ImAttributeKey;
import org.example.constant.ImMessageConstans;
import org.example.dto.ImMessageBody;
import org.example.enums.ImMessageEnum;
import org.example.handler.ImMessageHandlerContext;
import org.example.handler.ImMessageStrategy;
import org.example.rpc.interfaces.ImTokenRpc;
import org.example.message.ImMessage;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class LoginMessageHandler implements ImMessageStrategy {


    @DubboReference
    private ImTokenRpc imTokenRpc;


    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void handleMessage(ChannelHandlerContext channelHandlerContext, ImMessage imMessage) {
        log.info("开始处理登录消息");

        byte[] body = imMessage.getBody();

        if (body==null||body.length==0){
            channelHandlerContext.close();
            return;
        }

        ImMessageBody imMessageBody = JSON.parseObject(new String(body), ImMessageBody.class);


        //消息处理逻辑
        String token = imMessageBody.getToken();
        String appid = imMessageBody.getAppid();

        if (Strings.isNullOrEmpty(token)){
            //没有token不能建立连接
            log.error("token empty,imMessage is ",imMessage);
            throw  new IllegalArgumentException("token empty");
        }

        //如果token不为空，通过token获取userId
        Long userId = imTokenRpc.getUserId(token);

        if (userId==null){
            log.error("userId empty,imMessage is ",imMessage);
            throw  new IllegalArgumentException("userId empty");
        }

        //获取到了token,封装userId
        imMessageBody.setUserId(userId);
        imMessageBody.setData("true");
        imMessage.setBody(JSON.toJSONBytes(imMessageBody));

        channelHandlerContext.attr(ImAttributeKey.USER_ID_ATTR).set(userId);
        channelHandlerContext.attr(ImAttributeKey.APP_ID).set(appid);

        //缓存当前用户登录的服务器ip
        String hostAddress = null;
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        String cacheKey = ImMessageConstans.USER_BIND_IP_CACHE_KEY + userId + ":" + appid;
        stringRedisTemplate.opsForValue().set(cacheKey,hostAddress,5, TimeUnit.MINUTES);

        //发送一次心跳信息
        ImMessage heartBeatMessage = new ImMessage();
        BeanUtils.copyProperties(imMessage,heartBeatMessage);
        heartBeatMessage.setCode(ImMessageEnum.HEART_BEAT_MESSAGE.getCode());
        ImMessageHandlerContext.doChannelRead(channelHandlerContext,heartBeatMessage);


        ChannelHandlerContextCache.add(userId,channelHandlerContext);

        channelHandlerContext.fireChannelRead(imMessage);

    }

}
