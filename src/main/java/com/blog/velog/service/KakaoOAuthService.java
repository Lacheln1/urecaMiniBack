package com.blog.velog.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.net.http.HttpHeaders;
import java.util.HashMap;

@Service
public class KakaoOAuthService {

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${kakao.token-uri}")
    private String kakaoTokenUri;

    @Value("${kakao.user-info-uri}")
    private String kakaoUserInfoUri;

    // ✅ 🔹 프론트에서 요청하면 이 URL을 반환하여 카카오 로그인 페이지로 이동하도록 함
    public String getKakaoLoginUrl() {
        return "https://kauth.kakao.com/oauth/authorize?client_id=" + kakaoClientId +
                "&redirect_uri=" + kakaoRedirectUri + "&response_type=code";
    }

    // ✅ 🔹 카카오 로그인 후 사용자 정보 저장
    private Map<String, Object> getKakaoUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        
        // 카카오 사용자 정보 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<Void> request = new HttpEntity<>(headers);

        // 카카오 API 요청
        ResponseEntity<Map> response = restTemplate.exchange(
                kakaoUserInfoUri, HttpMethod.GET, request, Map.class);

        // 응답 데이터 확인
        Map<String, Object> kakaoAccount = (Map<String, Object>) response.getBody().get("kakao_account");
        String email = (String) kakaoAccount.get("email");

        // 사용자 정보 반환
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("email", email);

        return userInfo;
    }
    }
   
}
