package org.example.constant;

public enum GateWayHeadEnum {

    USER_LOGIN_ID("userId");


    private String name;

    GateWayHeadEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
