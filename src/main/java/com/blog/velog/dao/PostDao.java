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
	
	void increaseLike(@Param("id") Long id) throws Exception;
	
	void decreaseLike(@Param("id") Long id) throws Exception;
	
	// username으로 게시글 가져오기
    List<Post> getPostsByUsername(String username);

    // 프로필 이미지 업데이트
    void updateProfileImage(Post post);
    
    void syncProfileImages() throws Exception;
    
    int updatePostImage(@Param("username") String username, @Param("imageUrl") String imageUrl);
	
	
}
