package org.example.enums;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


public enum LivingEnum {


    DEFAULT_ROOM_TYE(1,"普通直播间"),

    PK_ROOM_TYE(2,"PK直播间");

    private Integer code;

    private String  desc;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    LivingEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
