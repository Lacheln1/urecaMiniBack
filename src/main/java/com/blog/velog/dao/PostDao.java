package com.blog.velog.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.blog.velog.dto.Post;

@Mapper
public interface PostDao {
	void insertPost(Post post);
	
	void updatePost(Post post);
	
	void deletePost(@Param("id") Long id);
}
