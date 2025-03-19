package com.blog.velog.dao;

import com.blog.velog.dto.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.Optional;

@Mapper
public interface MemberDao {
    void insertMember(Member member);
    
    Optional<Member> findByUsername(@Param("username") String username);
    
    Optional<Member> getMemberByEmail(@Param("email") String email);
    
    void updateMember(@Param("email") String email, @Param("github") String github, @Param("twitter") String twitter, @Param("website") String website);
    
    void deleteLoginInfo(@Param("email") String email);
    
    int updateEmail(@Param("email") String email, @Param("newEmail") String newEmail);
    
    void updatePassword(@Param("email") String email, @Param("password") String password);
    
    void updateSalt(@Param("email") String email, @Param("salt") String salt);
    
    int updateProfileImage(@Param("email") String email, @Param("profileImage") String profileImage);
}
