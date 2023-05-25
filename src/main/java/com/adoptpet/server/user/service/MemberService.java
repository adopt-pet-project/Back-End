package com.adoptpet.server.user.service;

import com.adoptpet.server.commons.security.dto.SecurityUserDto;
import com.adoptpet.server.commons.util.ConstantUtil;
import com.adoptpet.server.commons.util.SecurityUtils;
import com.adoptpet.server.user.domain.Member;
import com.adoptpet.server.user.domain.ProfileImage;
import com.adoptpet.server.user.dto.request.RegisterDto;
import com.adoptpet.server.user.dto.response.MemberResponseDto;
import com.adoptpet.server.user.repository.MemberRepository;
import com.adoptpet.server.user.repository.ProfileImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ProfileImageRepository profileImageRepository;
    private final MemberQueryService queryService;


    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Member findByMemberNo(Integer memberNo) {
        return memberRepository.findById(memberNo)
                .orElseThrow(IllegalStateException::new);
    }

    @Transactional
    public Member save(RegisterDto registerDto) {
        // 이메일로 회원을 조회해서 이미 있다면 예외발생
        memberRepository.findByEmail(registerDto.getEmail())
                .ifPresent(member -> {throw new IllegalArgumentException("이미 존재하는 회원입니다.");});

        Member member = registerDto.toEntity();

        if (Objects.isNull(registerDto.getImgNo())) {
            // 첨부된 사진이 없을 경우 기본 이미지를 등록한다.
            member.addProfileImage(ConstantUtil.DEFAULT_PROFILE);
        }

        // 회원을 저장한다.
        Member savedMember = memberRepository.save(member);
        System.out.println("member = " + member);

        if (Objects.nonNull(registerDto.getImgNo())) {
            // 첨부된 사진이 있을 경우, 현재 회원의 프로필 사진으로 업데이트 해준다.
            profileImageRepository.updateProfileImage(savedMember.getMemberNo(), registerDto.getImgNo());
        }

        return savedMember;
    }

    // 회원탈퇴 메서드
    @Transactional
    public void removeMember(SecurityUserDto user) {
        Member findMember = memberRepository.findById(user.getMemberNo())
                .orElseThrow(IllegalStateException::new);

        memberRepository.delete(findMember);
    }

    public MemberResponseDto findMemberInfo(Integer memberNo) {
        MemberResponseDto memberResponseDto = memberNo == 0 ? queryService.getUserInfo(SecurityUtils.getUser().getMemberNo()) :
                queryService.getUserInfo(memberNo);

        if (Objects.isNull(memberResponseDto)) {
            throw new IllegalArgumentException("조회하려는 회원은 없는 회원입니다.");
        }

        return memberResponseDto;

    }


}
