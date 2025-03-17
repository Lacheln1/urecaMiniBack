package com.blog.velog.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.blog.velog.dto.Post;

@Mapper
public interface PostDao {
	
	public List<Post> getAllPosts() throws Exception;
	
	void insertPost(Post post) throws Exception;
	
	void updatePost(Post post) throws Exception;
	
	void deletePost(@Param("id") Long id) throws Exception;
	
	Post getPostDetail(@Param("id") Long id) throws Exception;
}
