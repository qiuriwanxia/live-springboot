package org.example.utils;

import org.example.constant.GateWayHeadEnum;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class LiveRequestUtil {

    public static Long getUserId(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String userIdHeader = requestAttributes.getRequest().getHeader(GateWayHeadEnum.USER_LOGIN_ID.getName());
        return userIdHeader==null?null:Long.valueOf(userIdHeader);
    }

}
