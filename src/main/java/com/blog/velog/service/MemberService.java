package com.blog.velog.service;

import com.blog.velog.dao.MemberDao;
import com.blog.velog.dao.SaltDao;
import com.blog.velog.dto.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class MemberService {

    private final MemberDao memberDao;
    private final SaltDao saltDao;

    public MemberService(MemberDao memberDao, SaltDao saltDao) {
        this.memberDao = memberDao;
        this.saltDao = saltDao;
    }

    @Transactional
    public String registerMember(Member member) {
        // 이메일 중복 검사
        if (memberDao.getMemberByEmail(member.getEmail()).isPresent()) {
            return "이미 가입된 이메일입니다.";
        }

        // 패스워드 유효성 검사 (예: 최소 8자리, 대문자 포함 등)
        if (!isValidPassword(member.getPassword())) {
            return "비밀번호는 최소 8자 이상, 대문자 1개 포함해야 합니다.";
        }

        // Salt 생성
        String salt = generateSalt();

        // 비밀번호 해시 암호화
        String hashedPassword = hashPassword(member.getPassword(), salt);

        // 암호화된 비밀번호와 salt 저장
        member.setPassword(hashedPassword);
        memberDao.insertMember(member);
        saltDao.saveSalt(member.getEmail(), salt);

        return "회원가입 성공!";
    }

    public boolean isValidPassword(String password) {
        return password.length() >= 8 && password.matches(".*[A-Z].*");
    }

    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }

    private String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] hashedBytes = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (Exception e) {
            throw new RuntimeException("암호화 오류", e);
        }
    }
}
