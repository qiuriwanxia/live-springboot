package org.example.enums;

public enum ImBizMessageEnum {

    LIVING_ROOM_IM_CHAT_BIZ("9001","直播间消息");

    private String code;

    private String desc;

    ImBizMessageEnum(String code, String desc) {
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
