package org.example.constant;

public enum CommonStatusEnum {

    PHONE_VAILD(1,"手机号有效");


    private int code;

    private String desc;


    CommonStatusEnum(int code, String desc) {
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
