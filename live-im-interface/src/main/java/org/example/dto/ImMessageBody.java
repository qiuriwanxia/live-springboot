package org.example.dto;


import java.io.Serial;
import java.io.Serializable;

public class ImMessageBody implements Serializable {

    @Serial
    private static final long serialVersionUID = 4879877061577941635L;
    private String appid;

    private Long userId;

    private String data;

    private String token;


    public static ImMessageBody buildSuccess(Long userId,String appid,String token){
        ImMessageBody imMessageBody = new ImMessageBody();
        imMessageBody.setData("true");
        imMessageBody.setToken(token);
        imMessageBody.setAppid(appid);
        imMessageBody.setUserId(userId);
        return imMessageBody;
    }

    public ImMessageBody() {
    }

    public ImMessageBody(String appid, Long userId, String data, String token) {
        this.appid = appid;
        this.userId = userId;
        this.data = data;
        this.token = token;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
