package org.example.service;

import org.example.dto.ImMessageBody;

public interface ImRouterService {
    boolean sendMsg(Long rUserId, ImMessageBody messageBody);
}
