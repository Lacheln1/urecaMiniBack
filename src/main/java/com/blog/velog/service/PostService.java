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
	
	public List<Post> getAllPosts()throws Exception{
		return postDao.getAllPosts();
	}
	
	 public Post getPostDetail(Long id) throws Exception {
	        return postDao.getPostDetail(id);
	    }
	
	public void insertPost(Post p) throws Exception{
		postDao.insertPost(p);
	}
	
	public void updatePost(Long id, Post p) throws Exception{
		p.setId(id);
		postDao.updatePost(p);
	}
	
	public void deletePost(Long id) throws Exception{
		postDao.deletePost(id);
	}
}
