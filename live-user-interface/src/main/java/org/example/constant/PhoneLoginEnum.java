package org.example.constant;

public enum PhoneLoginEnum {

    PHONE_EMPTY("ERROR","手机号为空"),
    PHONE_NO_EXIT("ERROR","手机号不存在");

    private String code;

    private String desc;

    PhoneLoginEnum(String code, String desc) {
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
