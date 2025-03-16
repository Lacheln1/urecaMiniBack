package com.blog.velog.dao;

import com.blog.velog.dto.Login;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Optional;

@Mapper
public interface LoginDao {

    // 🔹 로그인 성공 시 기록 저장
    void insertLoginInfo(@Param("email") String email, 
                         @Param("pwd") String pwd, 
                         @Param("token") String token, 
                         @Param("status") String status);

    // 🔹 특정 이메일의 로그인 기록 조회
    Optional<Login> getLoginInfoByEmail(@Param("email") String email);

    // 🔹 로그아웃 시 로그인 기록 삭제
    void deleteLoginInfo(@Param("email") String email);
}
