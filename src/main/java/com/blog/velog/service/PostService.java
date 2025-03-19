package com.blog.velog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.velog.dao.PostDao;
import com.blog.velog.dto.Post;

@Service
public class PostService {
	@Autowired
	PostDao postDao;
	
	public List<Post> getAllPosts(){
		try {
			return postDao.getAllPosts();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new RuntimeException("게시글 목록 가져오기 실패", e);
		}
	}
	
	 public Post getPostDetail(Long id)  {
	       try {
	    	   return postDao.getPostDetail(id);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new RuntimeException("게시글 목록 가져오기 실패", e);
		}
	    }
	
	public void insertPost(Post p) throws Exception{
		try {
			postDao.insertPost(p);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new RuntimeException("게시글 작성 실패", e);
		}
	}
	
	public void updatePost(Long id, Post p) throws Exception{
		try {
			p.setId(id);
			postDao.updatePost(p);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new RuntimeException("게시글 수정 실패",e);
		}
		
	}
	
	public void deletePost(Long id) throws Exception{
		try {
			postDao.deletePost(id);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new RuntimeException("게시글 삭제 실패",e);
		}
		
	}
	
	public void increaseLike(Long id) throws Exception{
		try {
			postDao.increaseLike(id);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new RuntimeException("좋아요 증가 실패",e);
		}
		
	}
	
	public void decreaseLike(Long id) throws Exception{
		try {
			postDao.decreaseLike(id);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new RuntimeException("좋아요 감소 실패",e);
		}
		
	}
	
	public void updateProfileImage(String username, Post p) throws Exception{
		try {
			p.setUsername(username);
			postDao.updateProfileImage(p);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new RuntimeException("프로필 이미지 업데이트 실패",e);
		}
		
	}
	
}	
