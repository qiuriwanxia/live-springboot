package org.example.service;

import org.example.dto.ImMessageBody;

public interface SingleMessageHanlder {

    void onMessageReceiver(Long ruserId, ImMessageBody imMessageBody);

}
