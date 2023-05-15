package com.adoptpet.server.user.service;

import com.adoptpet.server.user.domain.Member;
import com.adoptpet.server.user.repository.MemberRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.adoptpet.server.user.domain.QMember.member;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JPAQueryFactory jpaQueryFactory;


    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Member findByMemberNo(Integer memberNo) {
        return memberRepository.findById(memberNo)
                .orElseThrow(IllegalStateException::new);
    }

    @Transactional
    public Member save(Member member) {
        return memberRepository.save(member);
    }

    public String getUserAddress(Integer id) {
        return jpaQueryFactory
                .select(member.address)
                .from(member)
                .where(member.memberNo.eq(id))
                .fetchOne();
    }
}
