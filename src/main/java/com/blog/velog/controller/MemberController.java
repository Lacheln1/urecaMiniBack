package com.blog.velog.controller;

import com.blog.velog.dto.Member;
import com.blog.velog.service.MemberService;
import com.blog.velog.util.JwtUtil;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
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
    
    // 로그인한 사용자 정보 가져오기
    @GetMapping("/profile")
    public ResponseEntity<?> getMemberProfile(@RequestHeader("Authorization") String token) {
        try {
            System.out.println("받은 토큰: " + token);

            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 요청 형식입니다.");
            }

            // Bearer  제거 후, 혹시 남아있을 수 있는 따옴표 제거
            String jwt = token.substring(7).replace("\"", ""); 
            System.out.println("실제 JWT 토큰: " + jwt);

            // JWT에서 이메일 추출
            String email = jwtUtil.extractEmail(jwt);
            System.out.println("추출된 이메일: " + email);

            // DB에서 사용자 정보 가져오기
            Optional<Member> memberOptional = memberService.getMemberByEmail(email);

            if (memberOptional.isPresent()) {
                Member member = memberOptional.get();
                return ResponseEntity.ok(member);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자 정보를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            System.out.println("JWT 검증 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT 토큰이 유효하지 않습니다.");
        }
    }



    @PutMapping("/update")
    public ResponseEntity<String> updateMember(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> request) {
        String email = jwtUtil.extractEmail(token.substring(7));
        
        String username = request.get("username");
        String bio = request.get("bio");
        String github = request.get("github");
        String twitter = request.get("twitter");
        String website = request.get("website");

        System.out.println("✅ 프로필 업데이트 요청: " + request);

        String result = memberService.updateMember(email, bio, github, twitter, website, username);

        return ResponseEntity.ok(result);
    }
    
    
    
    
    @PostMapping("/verify-password")
    public ResponseEntity<String> verifyPassword(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> request) {
        String email = jwtUtil.extractEmail(token.substring(7));
        String currentPassword = request.get("currentPassword");

        if (currentPassword == null || currentPassword.isEmpty()) {
            return ResponseEntity.badRequest().body("비밀번호를 입력하세요.");
        }

        boolean isValid = memberService.verifyPassword(email, currentPassword);

        if (isValid) {
            return ResponseEntity.ok("비밀번호 확인 성공");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 일치하지 않습니다.");
        }
    }
    
    @PutMapping("/update-email")
    public ResponseEntity<String> updateEmail(@RequestHeader("Authorization") String token, 
                                              @RequestBody Map<String, String> request) {
        String email = jwtUtil.extractEmail(token.substring(7));
        String newEmail = request.get("newEmail");
        String currentPassword = request.get("currentPassword");

        if (newEmail == null || newEmail.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("새 이메일이 제공되지 않았습니다.");
        }

        if (currentPassword == null || currentPassword.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("비밀번호가 제공되지 않았습니다.");
        }

        String result = memberService.updateEmail(email, newEmail, currentPassword);

        return ResponseEntity.ok(result);
    }
    
    
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestHeader("Authorization") String token,
                                                @RequestBody Map<String, String> request) {
        String email = jwtUtil.extractEmail(token.substring(7));  
        String response = memberService.changePassword(email, request.get("currentPassword"), request.get("newPassword"));
        return ResponseEntity.ok(response);
    }

    



}
