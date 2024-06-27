package org.example;

import org.example.dto.ImMessageBody;

public interface ImRouterRpc {
     void sendMsg(Long rUserId, ImMessageBody imMessageBody);

}
