package org.example.service;

import org.example.dto.ImMessageBody;

public interface ImRouterHandlerService {

    void onReceive(Long rUserId, ImMessageBody messageBody);

}
