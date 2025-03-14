package com.blog.velog.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.*;

public class Member {

    private Long id;
    private String name;
    private String profile_Image;
    private LocalDateTime createdAt = LocalDateTime.now();

    @NotBlank(message = "유저네임을 입력해주세요.")  
    @Size(min = 3, max = 50, message = "유저네임은 3~50자 사이여야 합니다.")
    private String username;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 형식을 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String password;

    
    public String profile_image() {return profile_Image;}
    public void profile_Image(String profile_Image) {this.profile_Image = profile_Image;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
