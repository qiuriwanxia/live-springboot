package org.example.interfaces;

import org.example.constant.MsgSendResultEnum;

public interface MsgRpc {


    MsgSendResultEnum sendMsg(String phone);

    MsgSendResultEnum checkMsgCode(String phone,String code);


}
