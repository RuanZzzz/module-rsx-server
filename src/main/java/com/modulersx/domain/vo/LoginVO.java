package com.modulersx.domain.vo;

public class LoginVO {

    private String token;
    private UserInfoVO userInfo;

    public LoginVO() {
    }

    public LoginVO(String token, UserInfoVO userInfo) {
        this.token = token;
        this.userInfo = userInfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserInfoVO getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoVO userInfo) {
        this.userInfo = userInfo;
    }
}
