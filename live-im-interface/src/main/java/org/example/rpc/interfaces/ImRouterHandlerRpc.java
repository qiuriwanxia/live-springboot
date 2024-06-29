package org.example.rpc.interfaces;

import org.example.dto.ImMessageBody;

public interface ImRouterHandlerRpc {

    /**
     * 将消息发送给指定用户
     * @param rUserId
     * @param messageBody
     */
    void   sendMsg(Long rUserId, ImMessageBody messageBody);

}
