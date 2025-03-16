package com.blog.velog.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.velog.dto.Post;
import com.blog.velog.service.PostService;

@RestController
@CrossOrigin("http://127.0.0.1:5500/")
public class PostController {
	
	@Autowired
	PostService postService;
	
	Map<String,Object> storage = new HashMap();
	
	@GetMapping("getAllPosts")
	public List<Post> getAllPosts(){
		try {
			Object o = storage.get("fistPagePosts");
			if(o==null) {
				List<Post> list = postService.getAllPosts();
				
				storage.put("fistPagePosts", list);
				return list;
			}
			return (List<Post>) o;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	@GetMapping("/getPostDetail")
	public ResponseEntity<Post> getPostDetail(@RequestParam Long id){
		try {
			Post post = postService.getPostDetail(id);
			return ResponseEntity.ok(post);
		} catch (Exception e) {
			// TODO: handle exception
			 return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
	
	@PostMapping("insertPost")
	public String insertPost(@RequestBody Post p) {
		System.out.println(p);
		try {
			postService.insertPost(p);
			return "게시했습니다!";
		} catch(Exception e) {
			e.printStackTrace();
			return "게시 실패";
		}
	}
	
	
	@PostMapping("updatePost")
	public String updatePost(@RequestBody Post p) {
		System.out.println(p);
		try {
			postService.updatePost(p);
			return"게시글 수정 완료";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return"게시글 수정 실패";
		}
	}
	
	@PostMapping("deletePost")
	public String deletePost(@RequestBody Long id) {
		System.out.println(id);
		try {
			postService.deletePost(id);
			return "글 삭제 완료";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "삭제 실패";
		}
	}
	
}
