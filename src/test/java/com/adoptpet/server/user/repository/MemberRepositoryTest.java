package com.adoptpet.server.user.repository;


import com.adoptpet.server.user.domain.Member;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("멤버 저장 테스트")
    void insertMember() {
        // given
        Member member = Member.builder()
                .email("ckdekrn88@gmail.com")
                .address("서울 마포구")
                .nickname("서울 개발자")
                .userRole("USER")
                .platform("google")
                .build();

        // when
        Member savedMember = memberRepository.save(member);

        // then
        assertThat(savedMember).isEqualTo(member);
        assertThat(savedMember.getEmail()).isEqualTo("ckdekrn88@gmail.com");
        assertThat(savedMember.getNickname()).isEqualTo("서울 개발자");
    }

    @Test
    @DisplayName("회원 조회 테스트")
    void findMember() {
        // given
        Member member = Member.builder()
                .email("ckdekrn88@gmail.com")
                .address("서울 마포구")
                .nickname("서울 개발자")
                .userRole("USER")
                .platform("google")
                .build();

        // when
        Member savedMember = memberRepository.save(member);
        Member findMember = memberRepository.findById(savedMember.getMemberNo())
                .orElseThrow(IllegalArgumentException::new);
        Member findByEmail = memberRepository.findByEmail(savedMember.getEmail())
                .orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(findMember).isEqualTo(savedMember).isEqualTo(findByEmail);
        assertThat(findMember.getEmail()).isEqualTo(savedMember.getEmail()).isEqualTo(findByEmail.getEmail());
        assertThat(findMember.getNickname()).isEqualTo(savedMember.getNickname()).isEqualTo(findByEmail.getNickname());
    }
}