package org.example.dto;

public class UserLoginDTO {


    private boolean loginStatus;

    private String phone;

    private String code;

    private String status;

    private String token;

    private Long userId;

    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(boolean loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public static UserLoginDTO loginSuccess(Long userId,String phone,String token){
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUserId(userId);
        userLoginDTO.setPhone(phone);
        userLoginDTO.setToken(token);
        userLoginDTO.setLoginStatus(true);
        return userLoginDTO;
    }

    public static UserLoginDTO loginError(String desc){
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setLoginStatus(false);
        userLoginDTO.setDesc(desc);
        return userLoginDTO;
    }
}
