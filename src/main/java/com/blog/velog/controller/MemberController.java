package com.blog.velog.controller;

import com.blog.velog.dto.Member;
import com.blog.velog.service.MemberService;
import com.blog.velog.util.JwtUtil;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class MemberController {
    
    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    public MemberController(MemberService memberService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerMember(@Valid @RequestBody Member member, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        
        String response = memberService.registerMember(member);
        if ("회원가입 성공!".equals(response)) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    //로그인 로직-> 토큰 발급
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        String token = memberService.authenticateMember(email, password);
        Optional<Member> member = memberService.getMemberByEmail(email);
        
        
        if (token != null) {
            Map<String, String> response = new HashMap<>();
            response.put("token", token.toString());
            response.put("email", email);
            response.put("username", member.get().getUsername());

            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body("로그인 실패: 이메일 또는 비밀번호가 올바르지 않습니다.");
    }

    
    
    //로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        if (email == null) {
            return ResponseEntity.badRequest().body("이메일이 제공되지 않았습니다.");
        }

        //  로그인 기록 삭제
        memberService.logoutMember(email);

        return ResponseEntity.ok("로그아웃 성공!");
    }

}
