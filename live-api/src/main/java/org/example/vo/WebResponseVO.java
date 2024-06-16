package org.example.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class WebResponseVO {

    private int code;

    private String msg;

    private Object data;

    public static WebResponseVO success(Object data){
        WebResponseVO webResponseVO = new WebResponseVO();
        webResponseVO.setCode(200);
        webResponseVO.setData(data);
        return webResponseVO;
    }

    public static WebResponseVO error(String msg){
        WebResponseVO webResponseVO = new WebResponseVO();
        webResponseVO.setCode(400);
        webResponseVO.setMsg(msg);
        return webResponseVO;
    }

}
