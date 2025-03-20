package com.blog.velog.service;

import com.blog.velog.dao.MemberDao;
import com.blog.velog.dao.SaltDao;
import com.blog.velog.dao.LoginDao; // LoginDao 추가
import com.blog.velog.dto.Login;
import com.blog.velog.dto.Member;
import com.blog.velog.util.JwtUtil;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
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
    public ResponseEntity<?> authenticateMember(String email, String password) {
        Optional<Member> optionalMember = memberDao.getMemberByEmail(email);
        Optional<String> blockTimeOpt = loginDao.getBlockTime(email);
        Optional<Login> loginInfoOpt = loginDao.getLoginInfoByEmail(email);
        
        System.out.println("🚀 [디버깅] 현재 차단 시간 조회: " + blockTimeOpt.orElse("NULL"));
        System.out.println("🚀 현재 시간: " + new Timestamp(System.currentTimeMillis()));

        int failCount = loginInfoOpt.map(Login::getFailCount).orElse(0);

        // 🚨 로그인 차단 시간 확인
        if (blockTimeOpt.isPresent() && failCount >= 5) {
            Timestamp blockTime = Timestamp.valueOf(blockTimeOpt.get());
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());

            if (currentTime.before(blockTime)) {
                long secondsLeft = (blockTime.getTime() - currentTime.getTime()) / 1000;
                long minutesLeft = secondsLeft / 60;

                String blockMessage = (minutesLeft > 0) ?
                        "로그인 차단됨! " + minutesLeft + "분 후에 다시 시도하세요." :
                        "로그인 차단됨! " + secondsLeft + "초 후에 다시 시도하세요.";

                System.out.println("🚨 차단 메시지 반환: " + blockMessage);

                // ✅ JSON 형식으로 응답 반환
                Map<String, String> response = new HashMap<>();
                response.put("errorMessage", blockMessage);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        }

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            String salt = saltDao.getSaltByEmail(email);
            String hashedPassword = hashPassword(password, salt);
            String storedPassword = member.getPassword();

            if (hashedPassword.equals(storedPassword)) {
                String token = jwtUtil.generateToken(email);
                loginDao.insertLoginInfo(email, hashedPassword, token, "Success");
                loginDao.resetFailCount(email);

                // ✅ 정상 로그인 응답
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                response.put("email", email);
                response.put("username", member.getUsername());

                return ResponseEntity.ok(response);
            } else {
                System.out.println("비밀번호가 일치하지 않음");
            }
        }

        // 로그인 실패 처리
        loginDao.insertLoginInfo(email, "", "", "Fail");
        loginDao.increaseFailCount(email);

        // 🚨 5회 이상 실패 시 차단
        if (failCount >= 5) {
            Optional<String> existingBlockTimeOpt = loginDao.getBlockTime(email);
            if (existingBlockTimeOpt.isEmpty() || Timestamp.valueOf(existingBlockTimeOpt.get()).before(new Timestamp(System.currentTimeMillis()))) {
                Timestamp newBlockTime = new Timestamp(System.currentTimeMillis() + (10 * 60 * 1000));
                System.out.println("🚀 새로운 차단 시간 설정: " + newBlockTime);
                loginDao.setBlockTime(email, newBlockTime.toString());
            }

            Map<String, String> response = new HashMap<>();
            response.put("errorMessage", "5회 이상 로그인 실패! 10분 후 다시 시도하세요.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        Map<String, String> response = new HashMap<>();
        response.put("errorMessage", "로그인 실패: 이메일 또는 비밀번호가 올바르지 않습니다.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
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
    public void updateSocialInfo(String email, String github, String twitter, String website) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("이메일이 필요합니다.");
        }

        memberDao.updateMember(email, github, twitter, website);
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
    
    @Transactional
    public  List<Member> getUserProfileImage(String username) {
		return memberDao.getUserProfileImage(username); 
    }


}