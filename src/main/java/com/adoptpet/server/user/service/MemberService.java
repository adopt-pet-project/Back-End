package com.adoptpet.server.user.service;

import com.adoptpet.server.commons.util.ConstantUtil;
import com.adoptpet.server.user.domain.Member;
import com.adoptpet.server.user.domain.ProfileImage;
import com.adoptpet.server.user.dto.request.RegisterDto;
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

        // 회원을 저장한다.
        Member member = memberRepository.save(registerDto.toEntity());

        if (Objects.nonNull(registerDto.getImgNo())) {
            // 첨부된 사진이 있을 경우, 현재 회원의 프로필 사진으로 업데이트 해준다.
            profileImageRepository.updateProfileImage(member.getMemberNo(), registerDto.getImgNo());
        }

        // 첨부된 사진이 없을 경우 기본 이미지를 등록한다.
        ProfileImage profileImage = getProfile(member);
        profileImageRepository.save(profileImage);

        return memberRepository.save(member);
    }


    private ProfileImage getProfile(Member member) {
        ProfileImage profileImage = ProfileImage.builder()
                .memberNo(member.getMemberNo())
                .build();

        profileImage.addRagId(member.getEmail());
        profileImage.addImageName("기본 이미지");
        profileImage.addImageType("기본 이미지");
        profileImage.addImageUrl(ConstantUtil.DEFAULT_PROFILE);

        return profileImage;
    }


}
