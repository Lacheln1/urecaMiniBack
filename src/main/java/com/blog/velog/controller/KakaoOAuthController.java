package com.blog.velog.controller;

import com.blog.velog.dto.Member;
import com.blog.velog.service.KakaoOAuthService;
import com.blog.velog.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/members/kakao")
public class KakaoOAuthController {

    private final KakaoOAuthService kakaoOAuthService;
    private final JwtUtil jwtUtil;

    @Autowired
    public KakaoOAuthController(KakaoOAuthService kakaoOAuthService, JwtUtil jwtUtil) {
        this.kakaoOAuthService = kakaoOAuthService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/callback")
    public void kakaoLogin(@RequestParam("code") String code, HttpServletResponse response) {
        try {
            System.out.println("🔵 카카오 로그인 요청: code = " + code);

            // 1. 카카오 로그인 처리
            Member member = kakaoOAuthService.registerOrLoginKakaoUser(code);
            System.out.println("✅ 로그인된 사용자 정보: " + member);

            // 2. JWT 토큰 생성
            String token = jwtUtil.generateToken(member.getEmail());
            System.out.println("🔐 발급된 JWT 토큰: " + token);

            // 3. index.html로 리다이렉트하면서 토큰 전달
            String redirectUrl = "http://localhost:5500/index.html"
                    + "?token=" + token
                    + "&username=" + URLEncoder.encode(member.getUsername(), StandardCharsets.UTF_8)
                    + "&email=" + URLEncoder.encode(member.getEmail(), StandardCharsets.UTF_8);

            System.out.println("🚀 리다이렉트 URL: " + redirectUrl);
            response.sendRedirect(redirectUrl);

        } catch (Exception e) {
            System.err.println("❌ 카카오 로그인 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            try {
                response.sendRedirect("http://localhost:5500/login.html?error=true");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
    
    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> kakaoLoginRedirect() {
        String kakaoRedirectUri = "https://kauth.kakao.com/oauth/authorize"
                + "?client_id=" + "YOUR_KAKAO_CLIENT_ID" // ✅ 여기에 실제 클라이언트 ID를 넣어야 함
                + "&redirect_uri=" + "http://localhost:8080/api/members/kakao/callback"
                + "&response_type=code";

        Map<String, String> response = new HashMap<>();
        response.put("redirect_url", kakaoRedirectUri);

        return ResponseEntity.ok(response);
    }
    
    
}
