package com.blog.velog.controller;

import com.blog.velog.dto.Member;
import com.blog.velog.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerMember(@Valid @RequestBody Member member, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors().get(0).getDefaultMessage());
        }
        memberService.registerMember(member);
        return ResponseEntity.ok("회원 가입 성공!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateMember(@PathVariable Long id, @Valid @RequestBody Member member, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors().get(0).getDefaultMessage());
        }
        member.setId(id);
        memberService.updateMember(member);
        return ResponseEntity.ok("회원 정보 수정 완료!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok("회원 삭제 완료!");
    }
}
