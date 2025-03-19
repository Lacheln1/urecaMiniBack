package com.blog.velog.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.blog.velog.dto.Member;

@Mapper
public interface MemberDao {
    void insertMember(Member member);
    
    Optional<Member> findByUsername(@Param("username") String username);
    
    Optional<Member> getMemberByEmail(@Param("email") String email);
    
    List<Member> getUserProfileImage(@Param("username") String username);
    
    int updateMember(@Param("params") Map<String, Object> params);
    
    int updateEmail(@Param("email") String email, @Param("newEmail") String newEmail);
    
    void updatePassword(@Param("email") String email, @Param("password") String password);
    
    void updateSalt(@Param("email") String email, @Param("salt") String salt);
    
    int updateProfileImage(@Param("email") String email, @Param("profileImage") String profileImage);
}
