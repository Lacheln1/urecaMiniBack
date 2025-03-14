package com.blog.velog.service;

import com.blog.velog.dao.MemberDao;
import com.blog.velog.dto.Member;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberDao memberDao;
    private final BCryptPasswordEncoder passwordEncoder;

    public MemberService(MemberDao memberDao, BCryptPasswordEncoder passwordEncoder) {
        this.memberDao = memberDao;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerMember(Member member) {
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        memberDao.insertMember(member);
    }

    public void updateMember(Member member) {
        if (member.getPassword() != null && !member.getPassword().isEmpty()) {
            member.setPassword(passwordEncoder.encode(member.getPassword()));
        }
        memberDao.updateMember(member);
    }

    public void deleteMember(Long id) {
        memberDao.deleteMember(id);
    }
}
