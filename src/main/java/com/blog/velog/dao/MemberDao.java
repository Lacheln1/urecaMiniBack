package com.blog.velog.dao;

import com.blog.velog.dto.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberDao {

    void insertMember(Member member);
    
    void updateMember(Member member);
    
    void deleteMember(@Param("id") Long id);
    
    Member findByUsername(@Param("username") String username);
}
