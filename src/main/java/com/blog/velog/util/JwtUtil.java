package com.blog.velog.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}") // application.properties에서 키 값 가져오기
    private String SECRET_KEY;

    private static final long EXPIRATION_TIME = 1000 * 60 * 1; // 30분

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes()) 
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .parseClaimsJws(token)
                    .getBody();

            Date expiration = claims.getExpiration();
            System.out.println("🔍 토큰 만료 시간: " + expiration);
            System.out.println("🔍 현재 시간: " + new Date());

            // 🔥 만료된 경우 false 반환
            if (expiration.before(new Date())) {
                System.out.println("⏳ JWT 토큰이 만료됨 → false 반환");
                return false;
            }

            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("⏳ JWT 토큰이 만료됨 → ExpiredJwtException 발생");
            return false;
        } catch (Exception e) {
            System.out.println("🚨 JWT 검증 중 오류 발생: " + e.getMessage());
            return false;
        }
    }

    
    public String refreshToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY.getBytes()) // 서명 검증
                    .parseClaimsJws(token)
                    .getBody();

            // 새로운 토큰 생성 (기존 정보 유지, 만료 시간만 변경)
            return Jwts.builder()
                    .setSubject(claims.getSubject()) // 기존 이메일 유지
                    .setIssuedAt(new Date()) // 현재 시간
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 30분 연장
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
                    .compact();
        } catch (Exception e) {
            return null; // 토큰이 유효하지 않으면 null 반환
        }
    }
    
}
