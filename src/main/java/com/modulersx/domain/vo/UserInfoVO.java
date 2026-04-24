package com.modulersx.domain.vo;

public class UserInfoVO {

    private String username;
    private String nickname;
    private String status;

    public UserInfoVO() {
    }

    public UserInfoVO(String username, String nickname, String status) {
        this.username = username;
        this.nickname = nickname;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
