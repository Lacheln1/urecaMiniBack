package com.blog.velog.service;

import com.blog.velog.dao.MemberDao;
import com.blog.velog.dao.SaltDao;
import com.blog.velog.dao.LoginDao; // LoginDao 추가
import com.blog.velog.dto.Member;
import com.blog.velog.util.JwtUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberDao memberDao;
    private final SaltDao saltDao;
    private final LoginDao loginDao; // 추가된 LoginDao
    private final JwtUtil jwtUtil;

    public MemberService(MemberDao memberDao, SaltDao saltDao, LoginDao loginDao, JwtUtil jwtUtil) {
        this.memberDao = memberDao;
        this.saltDao = saltDao;
        this.loginDao = loginDao;
        this.jwtUtil = jwtUtil;
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
    
    // 이메일로 회원 정보 가져오기
    public Optional<Member> getMemberByEmail(String email) {
        return memberDao.getMemberByEmail(email);
    }
    
    

    // 로그인 -> 비밀번호 검증 -> JWT 발급 -> 로그인 정보 저장
    @Transactional
    public String authenticateMember(String email, String password) {
        Optional<Member> optionalMember = memberDao.getMemberByEmail(email);

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            String salt = saltDao.getSaltByEmail(email);
            String hashedPassword = hashPassword(password, salt);

            if (hashedPassword.equals(member.getPassword())) {
                String token = jwtUtil.generateToken(email);

                //  로그인 성공 시 DB에 기록 저장
                loginDao.insertLoginInfo(email, hashedPassword, token, "Success");

                return token;
            }
        }

        //  로그인 실패 시 기록 저장
        loginDao.insertLoginInfo(email, "", "", "Fail");
        return null;
    }

    // 로그아웃 기능
    @Transactional
    public void logoutMember(String email) {
        loginDao.deleteLoginInfo(email); //  로그인 기록 삭제
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
