package com.blog.velog.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.blog.velog.dto.Post;
import com.blog.velog.service.MemberService;
import com.blog.velog.service.PostService;
import com.blog.velog.util.JwtUtil;

import jakarta.validation.Valid;

@RestController
@CrossOrigin("http://127.0.0.1:5500/")
public class PostController {
	
	@Autowired
	PostService postService;
	
	@Autowired
	MemberService memberService;
	
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
	public String updatePost(@Valid @PathVariable Long id, @RequestBody Post p) {
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
	
	@PutMapping("updateProfileImage/{username}")
	public String updateProfileImage(@Valid @PathVariable String username, @RequestBody Post p) {
		System.out.println(p);
		try {
			postService.updateProfileImage(username, p);
			return "프로필 이미지 업데이트 완료";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "업데이트 실패";
		}
	}
	
	 @PutMapping("/syncProfileImages")
	    public ResponseEntity<String> syncProfileImages() {
	        try {
	            postService.syncProfileImages();
	            return ResponseEntity.ok("프로필 테이블 동기화 성공");
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error synchronizing profile images.");
	        }
	 }
	 
}
