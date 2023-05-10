package com.adoptpet.server.adopt.service;

import com.adoptpet.server.adopt.repository.MemberRepository;
import com.adoptpet.server.user.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;


    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Transactional
    public Member save(Member member) {
        return memberRepository.save(member);
    }
}
