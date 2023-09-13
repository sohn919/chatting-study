package com.server.chatting.controller;

import com.server.chatting.dto.LoginDTO;
import com.server.chatting.entity.member.Member;
import com.server.chatting.entity.member.MemberRepository;
import com.server.chatting.error.exception.Exception404;
import com.server.chatting.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Controller
@Slf4j
public class MemberController {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        Member member = memberRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new Exception404("유저가 없습니다."));

        String token = jwtProvider.generateToken(member);

        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
