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
import java.util.HashMap;
import java.util.Map;
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
        byte[] saltBytes = new byte[3]; 
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
    
    //사용자 정보 업데이트
    
    @Transactional
    public String updateMember(String email, String bio, String github, String twitter, String website, String username) {
        if (email == null || email.isEmpty()) {
            return "요청에 이메일이 포함되어 있지 않습니다.";
        }

        Optional<Member> optionalMember = memberDao.getMemberByEmail(email);
        if (optionalMember.isEmpty()) {
            return "회원 정보를 찾을 수 없습니다.";
        }

        Map<String, Object> updateParams = new HashMap<>();
        updateParams.put("email", email);

        if (bio != null && !bio.isEmpty()) updateParams.put("bio", bio);
        if (github != null && !github.isEmpty()) updateParams.put("github", github);
        if (twitter != null && !twitter.isEmpty()) updateParams.put("twitter", twitter);
        if (website != null && !website.isEmpty()) updateParams.put("website", website);
        if (username != null && !username.isEmpty()) updateParams.put("username", username);

        System.out.println("🔥 업데이트할 데이터: " + updateParams);

        int rowsUpdated = memberDao.updateMember(updateParams);
        System.out.println("🔥 업데이트 완료, 변경된 행 수: " + rowsUpdated);

        if (rowsUpdated == 0) {
            return "업데이트 실패: 해당 이메일을 찾을 수 없습니다.";
        }

        return "회원정보가 성공적으로 업데이트되었습니다.";
    }





    
    public boolean verifyPassword(String email, String currentPassword) {
        Optional<Member> optionalMember = memberDao.getMemberByEmail(email);
        if (optionalMember.isEmpty()) {
            return false; // 사용자가 존재하지 않음
        }

        Member member = optionalMember.get();
        String salt = saltDao.getSaltByEmail(email);
        String hashedPassword = hashPassword(currentPassword, salt);

        return hashedPassword.equals(member.getPassword()); // 비밀번호가 일치하는지 확인
    }
    
    @Transactional
    public String updateEmail(String email, String newEmail, String currentPassword) {
        Optional<Member> optionalMember = memberDao.getMemberByEmail(email);
        if (optionalMember.isEmpty()) {
            return "회원 정보를 찾을 수 없습니다.";
        }

        // 현재 비밀번호 검증
        boolean isValid = verifyPassword(email, currentPassword);
        if (!isValid) {
            return "비밀번호가 일치하지 않습니다.";
        }

        // member 테이블에서 이메일 변경
        int memberRowsUpdated = memberDao.updateEmail(email, newEmail);
        
        // salt_info 테이블에서도 이메일 변경 추가
        int saltRowsUpdated = saltDao.updateSaltEmail(email, newEmail); // ✅ 추가된 부분

        System.out.println("🔥 member 변경된 행 수: " + memberRowsUpdated);
        System.out.println("🔥 salt_info 변경된 행 수: " + saltRowsUpdated);

        // 이메일이 member 테이블과 salt_info 테이블에서 모두 변경되었는지 확인
        if (memberRowsUpdated == 0 || saltRowsUpdated == 0) {
            return "이메일 변경 실패: 일부 데이터가 업데이트되지 않음";
        }

        return "이메일이 성공적으로 변경되었습니다.";
    }


    @Transactional
    public String changePassword(String email, String currentPassword, String newPassword) {
        // 이메일로 회원 정보 조회
        Optional<Member> optionalMember = memberDao.getMemberByEmail(email);
        if (optionalMember.isEmpty()) {
            return "회원 정보를 찾을 수 없습니다.";
        }

        Member member = optionalMember.get();
        String salt = saltDao.getSaltByEmail(email); // 기존 salt 가져오기

        // 현재 비밀번호 검증
        if (!verifyPassword(email, currentPassword)) {
            return "현재 비밀번호가 일치하지 않습니다.";
        }

        // 새로운 솔트 생성 및 비밀번호 암호화
        String newSalt = generateSalt();
        String hashedNewPassword = hashPassword(newPassword, newSalt);

        // 업데이트 실행
        memberDao.updatePassword(email, hashedNewPassword);
        saltDao.updateSalt(email, newSalt);

        return "비밀번호가 성공적으로 변경되었습니다.";
    }
    
    
    @Transactional
    public String updateProfileImage(String email, String profileImageUrl) {
        memberDao.updateProfileImage(email, profileImageUrl);
        return "프로필 이미지가 업데이트되었습니다.";
    }

    
    @Transactional
    public String removeProfileImage(String email) {
        Optional<Member> optionalMember = memberDao.getMemberByEmail(email);
        if (optionalMember.isEmpty()) {
            return "회원 정보를 찾을 수 없습니다.";
        }

        int rowsUpdated = memberDao.updateProfileImage(email, null); // 이미지 제거 (NULL 값)
        if (rowsUpdated == 0) {
            return "프로필 이미지 제거 실패!";
        }

        return "프로필 이미지가 성공적으로 제거되었습니다.";
    }
    



}