package org.example.interfaces.enums;

public enum IdBuilderEnum {


    USER_ID_STRATEGY(1,"用户id生成策略");

    private int code;

    private String desc;

    IdBuilderEnum(int code, String desc) {
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
