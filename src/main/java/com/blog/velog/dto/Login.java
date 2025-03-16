package com.blog.velog.dto;

import java.sql.Timestamp;

public class Login {
    private String email;
    private String pwd;
    private String token;
    private String loginStatus;
    private Timestamp firstLogin;

    // 생성자
    public Login() {}

    public Login(String email, String pwd, String token, String loginStatus, Timestamp firstLogin) {
        this.email = email;
        this.pwd = pwd;
        this.token = token;
        this.loginStatus = loginStatus;
        this.firstLogin = firstLogin;
    }

    // Getter & Setter
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPwd() { return pwd; }
    public void setPwd(String pwd) { this.pwd = pwd; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getLoginStatus() { return loginStatus; }
    public void setLoginStatus(String loginStatus) { this.loginStatus = loginStatus; }

    public Timestamp getFirstLogin() { return firstLogin; }
    public void setFirstLogin(Timestamp firstLogin) { this.firstLogin = firstLogin; }
}
