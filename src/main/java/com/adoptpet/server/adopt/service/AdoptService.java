package com.adoptpet.server.adopt.service;

import com.adoptpet.server.adopt.domain.Adopt;
import com.adoptpet.server.adopt.domain.AdoptBookmark;
import com.adoptpet.server.adopt.domain.AdoptStatus;
import com.adoptpet.server.adopt.dto.request.AdoptImageRequestDto;
import com.adoptpet.server.adopt.dto.request.AdoptRequestDto;
import com.adoptpet.server.adopt.dto.request.AdoptStatusRequestDto;
import com.adoptpet.server.adopt.dto.response.AdoptDetailResponseDto;
import com.adoptpet.server.adopt.repository.AdoptBookmarkRepository;
import com.adoptpet.server.adopt.repository.AdoptImageRepository;
import com.adoptpet.server.adopt.repository.AdoptRepository;
import com.adoptpet.server.commons.security.dto.SecurityUserDto;
import com.adoptpet.server.commons.util.ConstantUtil;
import com.adoptpet.server.commons.util.SecurityUtils;
import com.adoptpet.server.user.domain.Member;
import com.adoptpet.server.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdoptService {
    private final AdoptRepository adoptRepository;
    private final AdoptImageRepository adoptImageRepository;
    private final AdoptBookmarkRepository adoptBookmarkRepository;
    private final AdoptQueryService queryService;
    private final MemberService memberService;

    public Adopt findBySaleNo(Integer saleNo) {
        return adoptRepository.findById(saleNo)
                .orElseThrow(IllegalStateException::new);
    }

    @Transactional
    public void deleteAdopt(Integer saleNo) {
        // 분양글과 북마크를 제거
        removeBookMarkAndAdopt(saleNo);
        // 삭제한 분양글과 관계가 있는 이미지의 key 값을 전부 null로 업데이트 한다.
        adoptImageRepository.updateAdoptImageNull(saleNo);
    }

    @Transactional
    public void removeBookMarkAndAdopt(Integer saleNo) {
        // 삭제하려는 분양글과 연관된 북마크부터 제거
        queryService.removeBookmark(saleNo);
        // 분양글을 제거
        adoptRepository.deleteBySaleNo(saleNo);
    }

    @Transactional
    public Adopt insertAdopt(AdoptRequestDto adoptDto, SecurityUserDto user) {
        // 회원의 정보로 Adopt 엔티티의 값을 셋팅해준다.
        Adopt adopt = getAdopt(adoptDto, user);
        // 분양 글을 저장한다.
        Adopt savedAdopt = adoptRepository.save(adopt);
        // 분양 글과 연관있는 이미지들의 데이터를 업데이트 해준다.
        updateAdoptImageSaleNo(adoptDto.getImage(), savedAdopt.getSaleNo());
        return savedAdopt;
    }


    @Transactional
    public Adopt updateAdopt(AdoptRequestDto adoptDto, SecurityUserDto user, Integer saleNo) {
        // 분양글의 고유번호로 분양글을 조회한다.
        Adopt adopt = findBySaleNo(saleNo);
        // AdoptRequestDto의 내용으로 분양글 엔티티의 내용을 변경한다.
        adopt.updateAdopt(adoptDto, user);
        // 이미지 배열 중 가장 첫번째 URL을 썸네일 이미지로 넣어준다.
        if (Objects.nonNull(adoptDto.getImage())) {
            adopt.addThumbnail(adoptDto.getImage()[0].getImgUrl());
        }
        // 분양 글을 업데이트 한다.
        Adopt updatedAdopt = adoptRepository.save(adopt);
        // 분양 글과 연관있는 이미지들의 데이터를 업데이트 해준다.
        updateAdoptImageSaleNo(adoptDto.getImage(), saleNo);
        return updatedAdopt;
    }

    @Transactional
    public void updateAdoptImageSaleNo(AdoptImageRequestDto[] imageDto, Integer saleNo) {
        if (!Objects.isNull(imageDto)) {
            // 분양 이미지 테이블의 saleNo 값을 분양 테이블의 saleNo 값으로 업데이트 해준다.
            for (int i=0; i< imageDto.length; i++) {
                adoptImageRepository.updateAdoptImageSaleNo(saleNo, imageDto[i].getImgNo(), (i+1));
            }
        }
    }

    @Transactional
    public void insertAdoptBookmark(SecurityUserDto dto, Integer saleNo) {
        // 회원의 정보와 현재 분양 게시글의 정보를 가져온다.
        Adopt adopt = findBySaleNo(saleNo);
        Member member = memberService.findByMemberNo(dto.getMemberNo());
        Optional<AdoptBookmark> findBookmark = adoptBookmarkRepository.findByMemberNoAndSaleNo(member.getMemberNo(), adopt.getSaleNo());
        if(findBookmark.isPresent()) {
            throw new IllegalStateException("이미 관심 분양글로 등록 되었습니다.");
        }

        // 외래키 업데이트를 위해 관심 분양 게시글 엔티티에 회원과 분양 게시글 엔티티를 셋팅해준다.
        AdoptBookmark adoptBookmark = new AdoptBookmark(dto.getEmail(), adopt, member);
        adoptBookmarkRepository.save(adoptBookmark);
    }

    public AdoptDetailResponseDto readAdopt(Integer saleNo, String accessToken, HttpServletRequest request, HttpServletResponse response) {
        // Adopt 테이블과 Member 테이블을 조인해서 가져올 수 있는 데이터를 먼저 채운다.
        AdoptDetailResponseDto responseDto = queryService.selectAdoptAndMember(saleNo);
        // 현재 분양 게시글과 관련이 있는 이미지 url을 조회해온다.
        String[] images = queryService.selectAdoptImages(saleNo).toArray(String[]::new);

        // 현재 분양 게시글을 보는 회원이 권한이 있는지 여부는 기본 false
        responseDto.addIsMine(false);

        if (StringUtils.hasText(accessToken)) {
            // 현재 분양 게시글을 보려는 회원이 이 분양 게시글을 작성한 작성자와 같은지 확인한다.
            boolean isMine = queryService.isMine(SecurityUtils.getUser().getEmail(), saleNo);
            responseDto.addIsMine(isMine);
        }

        // 현재 비어있는 responseDto의 이미지 필드의 값을 채워준다.
        responseDto.addImages(images);

        // 분양글의 조회수를 증가시키는 로직 실행
        increaseCount(saleNo, request, response);
        return responseDto;
    }

    @Transactional
    public void updateAdoptStatus(AdoptStatusRequestDto requestDto) {
        // 문자열로 되어있는 상태 값을 ENUM으로 변환
        AdoptStatus status = AdoptStatus.valueOf(requestDto.getStatus().toUpperCase());
        // 상태값 업데이트 진행
        adoptRepository.updateAdoptStatus(status, requestDto.getId());
    }

    @Transactional
    private void increaseCount(Integer saleNo, HttpServletRequest request, HttpServletResponse response) {
        Cookie oldCookie = null;
        // 현재 브라우저의 쿠키를 전부 가져온다.
        Cookie[] cookies = request.getCookies();

        // 쿠키가 있을 경우 실행
        if (Objects.nonNull(cookies)) {
            // 반복문을 돌면서 adoptView라는 이름을 가진 쿠키가 있을 경우 oldCookie에 값을 담아준다.
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("adoptView")) {
                    oldCookie = cookie;
                }
            }
        }

        // adoptView 쿠키가 null이 아닐경우 실행한다.
        if (Objects.nonNull(oldCookie)) {
            // 현재 분양글 번호를 값으로 포함한 쿠키가 없을 경우 실행한다.
            if (!oldCookie.getValue().contains("[" + saleNo + "]")) {
                // 조회수를 1 증가시킨다.
                adoptRepository.increaseCount(saleNo);
                // 현재 쿠키의 값에 조회한 분양글 번호를 이어서 저장해준다.
                oldCookie.setValue(oldCookie.getValue() + "_[" + saleNo + "]");
                // 이 쿠키는 모든 요청에 같이 전달되도록 설정
                oldCookie.setPath("/");
                // 쿠키의 유효기간을 하루로 설정
                oldCookie.setMaxAge(60 * 60 * 24);
                // 응답 객체(response)에 쿠키를 셋팅해준다.
                response.addCookie(oldCookie);
            }
        } else {
            // oldCookie의 값이 없다면 현재 조회한 게시글이 하나도 없는 상태이므로 조회 수 카운트를 올려준다.
            adoptRepository.increaseCount(saleNo);
            // 쿠키를 새로 생성하면서 현재 게시글의 번호를 값으로 넣어준다.
            Cookie newCookie = new Cookie("adoptView", "[" + saleNo + "]");
            // 이 쿠키는 모든 요청에 같이 전달되도록 설정
            newCookie.setPath("/");
            // 쿠키의 유효기간을 하루로 설정
            newCookie.setMaxAge(60 * 60 * 24);
            // 응답 객체(response)에 쿠키를 셋팅해준다.
            response.addCookie(newCookie);
        }

    }


    private Adopt getAdopt(AdoptRequestDto adoptDto, SecurityUserDto user) {
        // AdoptRequestDto => Adopt Entity로 변환해서 정보를 저장한다.
        Adopt adopt = adoptDto.toEntity();

        // 이미지 객체가 비어있지 않다면, 가장 첫 이미지를 대표 이미지로 지정해준다.
        if (Objects.nonNull(adoptDto.getImage())) {
            adopt.addThumbnail(adoptDto.getImage()[0].getImgUrl());
        }

        // 이미지 객체가 비어있다면 기본 분양 이미지를 넣어준다.
        adopt.addThumbnail(ConstantUtil.DEFAULT_ADOPT_IMAGE);

        // 등록자 ID와 수정자 ID를 넣어준다.
        adopt.addRegIdAndModId(user.getEmail(), user.getEmail());
        return adopt;
    }

}
