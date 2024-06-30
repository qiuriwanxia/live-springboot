package org.example;

import org.example.dto.ImMessageBody;

public interface ImRouterRpc {
     boolean sendMsg(Long rUserId, ImMessageBody imMessageBody);

}
