package com.blog.velog.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.blog.velog.dto.Post;

@Mapper
public interface PostDao {
	void insertPost(Post post) throws Exception;
	
	void updatePost(Post post) throws Exception;
	
	void deletePost(@Param("id") Long id) throws Exception;
}
