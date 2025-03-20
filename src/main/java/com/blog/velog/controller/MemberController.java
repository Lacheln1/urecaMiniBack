package com.blog.velog.controller;

import com.blog.velog.dto.Member;
import com.blog.velog.service.MemberService;
import com.blog.velog.util.JwtUtil;

import jakarta.validation.Valid;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/members")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class MemberController {
    
    private final MemberService memberService;
    private final JwtUtil jwtUtil;
    
    @Value("${file.upload-dir}")  // application.properties에서 업로드 경로 가져오기
    private String uploadDir;
    

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

        return memberService.authenticateMember(email, password);
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


    
    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfileInfo(@RequestBody Map<String, String> requestData) {
        String email = requestData.get("email");
        String username = requestData.get("username");
        String bio = requestData.get("bio");

        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("이메일이 필요합니다.");
        }

        memberService.updateProfileInfo(email, username, bio);
        return ResponseEntity.ok("프로필 정보가 성공적으로 업데이트되었습니다.");
    }
    

    @PutMapping("/updateSocialInfo")
    public ResponseEntity<?> updateSocialInfo(@RequestBody Map<String, String> requestData) {
        String email = requestData.get("email");
        String github = requestData.getOrDefault("github", "");
        String twitter = requestData.getOrDefault("twitter", "");
        String website = requestData.getOrDefault("website", "");

        memberService.updateSocialInfo(email, github, twitter, website);
        return ResponseEntity.ok("소셜 정보가 성공적으로 업데이트되었습니다.");
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
    
    
    
    @PostMapping("/upload-profile-image")
    public ResponseEntity<?> uploadProfileImage(
            @RequestHeader("Authorization") String token,
            @RequestParam("file") MultipartFile file) {
        
        String email = jwtUtil.extractEmail(token.substring(7));

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("업로드할 파일이 없습니다.");
        }

        try {
            // 저장할 경로 확인
            String uploadDir = "uploads/";  // 최종적으로 어디에 저장되는지 확인
            Path uploadPath = Paths.get(uploadDir);
            
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                System.out.println("로드 디렉토리 생성됨: " + uploadPath.toAbsolutePath());
            }

            String fileName = email + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            System.out.println("파일이 저장될 경로: " + filePath.toAbsolutePath());

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String profileImageUrl = "/uploads/" + fileName;  // DB에 저장되는 URL
            memberService.updateProfileImage(email, profileImageUrl);

            System.out.println("프로필 이미지 저장 성공: " + profileImageUrl);

            Map<String, String> response = new HashMap<>();
            response.put("profileImageUrl", profileImageUrl);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            System.out.println("파일 저장 오류: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 저장 실패");
        }
    }
    

    // 프로필 이미지 삭제 API
    @PutMapping("/remove-profile-image")
    public ResponseEntity<String> removeProfileImage(@RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractEmail(token.substring(7));

        try {
            Optional<Member> optionalMember = memberService.getMemberByEmail(email);
            if (optionalMember.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자 정보를 찾을 수 없습니다.");
            }

            Member member = optionalMember.get();
            String profileImagePath = member.getProfileImage();  // 현재 프로필 이미지 경로

            // 기본 이미지인 경우 삭제하지 않음
            if (profileImagePath == null || profileImagePath.equals("/uploads/no-intro.png")) {
                return ResponseEntity.ok("기본 이미지이므로 삭제할 필요가 없습니다.");
            }

            // 실제 파일 삭제
            File file = new File("uploads/" + profileImagePath.replace("/uploads/", ""));
            if (file.exists()) {
                boolean deleted = file.delete(); // 파일 삭제
                if (!deleted) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 삭제 실패!");
                }
            } else {
                System.out.println("🚨 삭제하려는 파일이 존재하지 않습니다: " + file.getAbsolutePath());
            }

            // DB에서 기본 이미지로 변경
            memberService.updateProfileImage(email, "/uploads/no-intro.png");

            return ResponseEntity.ok("프로필 이미지가 기본 이미지로 변경되었습니다.");
        } catch (Exception e) {
            System.out.println("🚨 이미지 삭제 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 제거 실패: " + e.getMessage());
        }
    }
    
}