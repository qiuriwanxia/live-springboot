package org.example.enums;

public enum  ImMessageAppEnum {


    LIVE_APP_BIZ("1","直播业务");

    private String code;

    private String desc;

    ImMessageAppEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
