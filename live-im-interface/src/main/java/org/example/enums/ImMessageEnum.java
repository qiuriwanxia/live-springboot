package org.example.enums;

public enum ImMessageEnum {


    LOGIN_MESSAGE(1001,"登录消息"),
    LOGIN_OUT_MESSAGE(1002,"登出消息"),
    BIZ_MESSAGE(1003,"业务消息"),
    HEART_BEAT_MESSAGE(1004,"心跳消息");

    private int code;

    private String desc;

    ImMessageEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
