package com.blog.velog.service;

import com.blog.velog.dao.MemberDao;
import com.blog.velog.dto.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class MemberService {
    
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Transactional
    public String registerMember(Member member) {
        // 중복 아이디(username) 검사
        if (memberDao.findByUsername(member.getUsername()).isPresent()) {
            return "이미 사용 중인 아이디입니다.";
        }

        // 비밀번호 암호화 (SHA-256)
        member.setPassword(encryptPassword(member.getPassword()));

        // 회원 정보 저장
        memberDao.insertMember(member);

        return "회원가입 성공!";
    }

    // SHA-256을 이용한 비밀번호 해싱
    private String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedPassword) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("비밀번호 암호화 오류");
        }
    }
}
