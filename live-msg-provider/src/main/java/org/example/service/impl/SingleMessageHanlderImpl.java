package org.example.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.example.ImRouterRpc;
import org.example.dto.ImMessageBody;
import org.example.dto.MessageDTO;
import org.example.enums.ImBizMessageEnum;
import org.example.service.SingleMessageHanlder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SingleMessageHanlderImpl implements SingleMessageHanlder {


    @DubboReference
    private ImRouterRpc imRouterRpc;

    @Override
    public boolean onMessageReceiver(Long ruserId, ImMessageBody imMessageBody) {
        String messageType = imMessageBody.getMessageType();
        if (ImBizMessageEnum.LIVING_ROOM_IM_CHAT_BIZ.getCode().equals(messageType)){
            //直播间聊天消息
            String data = imMessageBody.getData();
            MessageDTO messageDTO = JSON.parseObject(data, MessageDTO.class);
            log.info("接收到直播间聊天消息 {}",data);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("sendUserid",messageDTO.getUserId());
            jsonObject.put("content",messageDTO.getContent());
            imMessageBody.setData(jsonObject.toJSONString());
            return imRouterRpc.sendMsg(ruserId, imMessageBody);
        }
        return true;
    }
}
