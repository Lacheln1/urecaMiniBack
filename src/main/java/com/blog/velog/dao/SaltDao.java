package com.blog.velog.dao;

import com.blog.velog.dto.Salt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SaltDao {
    void saveSalt(@Param("email") String email, @Param("salt") String salt);
    
    String getSaltByEmail(@Param("email") String email);
    
    int updateSaltEmail(@Param("email") String email, @Param("newEmail") String newEmail);
    
    void updateSalt(@Param("email") String email, @Param("newSalt") String newSalt);
    
}
