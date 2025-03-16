package com.blog.velog.dao;

import com.blog.velog.dto.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Optional;

@Mapper
public interface MemberDao {
    void insertMember(Member member);
    Optional<Member> findByUsername(@Param("username") String username);
}
