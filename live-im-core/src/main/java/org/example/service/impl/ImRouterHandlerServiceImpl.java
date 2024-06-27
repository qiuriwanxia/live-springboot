package org.example.service.impl;


import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.example.cache.ChannelHandlerContextCache;
import org.example.constant.ImMessageMagic;
import org.example.dto.ImMessageBody;
import org.example.enums.ImMessageEnum;
import org.example.message.ImMessage;
import org.example.service.ImRouterHandlerService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ImRouterHandlerServiceImpl implements ImRouterHandlerService {
    @Override
    public void onReceive(Long rUserId, ImMessageBody messageBody) {
        //获取绑定的netty上下文对象
        ChannelHandlerContext channelHandlerContext = ChannelHandlerContextCache.get(rUserId);
        if (channelHandlerContext==null){
            //todo 用户此时下线了
            return;
        }

        Channel channel = channelHandlerContext.channel();
        ImMessage imMessage = new ImMessage();
        imMessage.setCode(ImMessageEnum.BIZ_MESSAGE.getCode());
        imMessage.setMagic(ImMessageMagic.magic);
        byte[] jsonBytes = JSON.toJSONBytes(messageBody);
        imMessage.setLength(jsonBytes.length);
        imMessage.setBody(jsonBytes);
        channel.writeAndFlush(imMessage);
    }
}
