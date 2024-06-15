package org.example.constant;

public enum MsgSendResultEnum {

    SEND_SUCCESS(0,"成功"),
    SEND_FAIL(1,"发送失败"),
    CHECK_SUCCESS(10,"验证码校验成功"),
    CHECK_ERROR(20,"验证码校验失败"),
    MSG_PARAM_ERROR(2,"消息格式异常");
    int code;
    String desc;
    MsgSendResultEnum(int code, String desc) {
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
