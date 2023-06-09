package com.adoptpet.server.user.service;

import com.adoptpet.server.commons.notification.repository.NotificationRepository;
import com.adoptpet.server.commons.security.dto.SecurityUserDto;
import com.adoptpet.server.commons.util.ConstantUtil;
import com.adoptpet.server.commons.util.SecurityUtils;
import com.adoptpet.server.community.repository.CommentRepository;
import com.adoptpet.server.community.repository.NoteHistoryRepository;
import com.adoptpet.server.user.domain.Member;
import com.adoptpet.server.user.dto.request.MemberModifyRequest;
import com.adoptpet.server.user.dto.request.RegisterDto;
import com.adoptpet.server.user.dto.response.MemberResponseDto;
import com.adoptpet.server.user.repository.MemberRepository;
import com.adoptpet.server.user.repository.ProfileImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ProfileImageRepository profileImageRepository;
    private final MemberQueryService queryService;
    private final CommentRepository commentRepository;
    private final NoteHistoryRepository noteHistoryRepository;
    private final NotificationRepository notificationRepository;


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
        commentRepository.deleteComment(findMember.getMemberNo());
        memberRepository.delete(findMember);
        deleteHistory(findMember.getMemberNo());
    }

    // 회원정보 수정 메서드
    @Transactional
    public void modifyMember(MemberModifyRequest modifyRequest, SecurityUserDto user) {
        // 토큰의 정보로 회원을 조회
        Member findMember = memberRepository.findById(user.getMemberNo())
                .orElseThrow(IllegalStateException::new);

        // 닉네임 수정의 경우 동작
        if (StringUtils.hasText(modifyRequest.getName()) && !modifyRequest.getName().equals(findMember.getNickname())) {
            // 현재 존재하는 닉네임인지 확인
            boolean isDuplicated = isDuplicated(modifyRequest.getName());

            // 닉네임 중복의 경우 500번 예외
            if (isDuplicated) {
                throw new IllegalStateException("중복된 닉네임 입니다.");
            }

            // 닉네임도 다시 셋팅
            findMember.changeNickname(modifyRequest.getName());
        }

        // Image도 함께 수정하는 경우 실행
        if (Objects.nonNull(modifyRequest.getImage())) {
            // 현재 프로필 이미지 삭제 -> 새로운 프로필 이미지로 업데이트 -> 회원 테이블에 이미지 URL 추가
            profileImageRepository.removeProfileImage(user.getMemberNo());
            profileImageRepository.updateProfileImage(user.getMemberNo(), modifyRequest.getImage().getImageKey());
            findMember.addProfileImage(modifyRequest.getImage().getImageUrl());
        }

        memberRepository.save(findMember);
    }

    // 닉네임 중복 확인 메서드
    public boolean isDuplicated(String nickname) {
        Optional<Member> findMember = memberRepository.findByNickname(nickname);
        return findMember.isPresent();
    }

    @Transactional
    public void deleteHistory(Integer memberNo) {
        noteHistoryRepository.deleteHistoryByReceiverNo(memberNo);
        noteHistoryRepository.deleteHistoryBySenderNo(memberNo);
        notificationRepository.deleteAllByMemberNo(memberNo);
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
