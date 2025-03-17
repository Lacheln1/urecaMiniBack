package com.blog.velog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.sql.Timestamp;

public class Member {
    
    private Long id; // Auto Increment Primary Key

    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    @Size(max = 50, message = "닉네임은 50자 이하로 입력해주세요.")
    private String username;

    @NotBlank(message = "이름(name)은 필수 입력값입니다.")
    @Size(max = 100, message = "이름(name)은 100자 이하로 입력해주세요.")
    private String name;

    @NotBlank(message = "이메일(email)은 필수 입력값입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @Size(max = 255, message = "이메일(email)은 255자 이하로 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호(password)는 필수 입력값입니다.")
    @Size(max = 255, message = "비밀번호(password)는 255자 이하로 입력해주세요.")
    private String password;

    private String profileImage; // 선택 입력값
    private Timestamp createdAt; // 자동 생성 (NOW())

    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
