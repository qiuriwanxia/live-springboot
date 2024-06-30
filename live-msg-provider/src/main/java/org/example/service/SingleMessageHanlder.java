package org.example.service;

import org.example.dto.ImMessageBody;

public interface SingleMessageHanlder {

    boolean onMessageReceiver(Long ruserId, ImMessageBody imMessageBody);

}
