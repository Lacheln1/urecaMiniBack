package com.blog.velog.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	public List<Post> getAllPosts() {
	    System.out.println("겟올포스트");
	    try {
	        return postService.getAllPosts(); 
	    } catch (Exception e) {
	        e.printStackTrace();
	        return Collections.emptyList();
	    }
	}
	
	@GetMapping("/getPostDetail/{id}")
	public ResponseEntity<Post> getPostDetail(@PathVariable Long id){
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
	
	
	@PutMapping("updatePost/{id}")
	public String updatePost(@PathVariable Long id, @RequestBody Post p) {
		System.out.println(p);
		try {
			postService.updatePost(id, p);
			System.out.println(p);
			return"게시글 수정 완료";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return"게시글 수정 실패";
		}
	}
	
	@DeleteMapping("/deletePost/{id}")
	public String deletePost(@PathVariable Long id) {
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
	
	@PostMapping("/{id}/like")
	public ResponseEntity<String> increaseLike(@PathVariable Long id){
		try {
			postService.increaseLike(id);
			return ResponseEntity.ok("좋아요 증가");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("좋아요 증가 실패");
		}
	}
	
	@PostMapping("/{id}/unlike")
	public ResponseEntity<String> decreaseLike(@PathVariable Long id){
		try {
			postService.decreaseLike(id);
			return ResponseEntity.ok("좋아요 감소");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("좋아요 감소 실패");
		}
	}
	
}
