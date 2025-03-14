package com.blog.velog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.velog.dao.PostDao;
import com.blog.velog.dto.Post;

@Service
public class PostService {
	@Autowired
	PostDao postDao;
	
	public void insertPost(Post p) throws Exception{
		postDao.insertPost(p);
	}
	
	public void updatePost(Post p) throws Exception{
		postDao.updatePost(p);
	}
	
	public void deletePost(Long id) throws Exception{
		postDao.deletePost(id);
	}
}
