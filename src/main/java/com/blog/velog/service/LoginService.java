package com.blog.velog.service;

import com.blog.velog.dao.MemberDao;
import com.blog.velog.dto.Member;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements UserDetailsService {

    private final MemberDao memberDao;

    public LoginService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberDao.findByUsername(username);

        if (member == null) {
            throw new UsernameNotFoundException("해당 사용자가 존재하지 않습니다: " + username);
        }

        return User.builder()
            .username(member.getUsername())
            .password(member.getPassword())
            .roles("USER")
            .build();
    }
}
