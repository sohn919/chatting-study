package com.server.chatting.service;

import com.server.chatting.entity.member.Member;
import com.server.chatting.entity.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    @Transactional
    public Optional<Member> findMemberById(Long memberId){
        return memberRepository.findById(memberId);
    }
}
