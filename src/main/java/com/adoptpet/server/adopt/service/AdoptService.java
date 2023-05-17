package com.adoptpet.server.adopt.service;

import com.adoptpet.server.adopt.domain.Adopt;
import com.adoptpet.server.adopt.domain.AdoptBookmark;
import com.adoptpet.server.adopt.dto.request.AdoptRequestDto;
import com.adoptpet.server.adopt.dto.response.AdoptResponseDto;
import com.adoptpet.server.adopt.repository.AdoptBookmarkRepository;
import com.adoptpet.server.adopt.repository.AdoptImageRepository;
import com.adoptpet.server.adopt.repository.AdoptRepository;
import com.adoptpet.server.commons.security.dto.SecurityUserDto;
import com.adoptpet.server.user.domain.Member;
import com.adoptpet.server.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

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
    public Adopt insertAdopt(AdoptRequestDto adoptDto, SecurityUserDto user) {
        // 현재 회원의 정보로 주소를 조회한다.
        String address = memberService.getUserAddress(user.getMemberNo());
        // 현재 회원의 주소 값을 셋팅한다.
        adoptDto.setAddress(address);
        // AdoptRequestDto => Adopt Entity로 변환해서 정보를 저장한다.
        Adopt adopt = adoptDto.toEntity();
        // 등록자 ID와 수정자 ID를 넣어준다.
        adopt.addRegIdAndModId(user.getEmail(), user.getEmail());
        // 분양 글을 저장한다.
        Adopt savedAdopt = adoptRepository.save(adopt);
        // 분양 글과 연관있는 이미지들의 데이터를 업데이트 해준다.
        updateAdoptImageSaleNo(adoptDto.getImgNo(), savedAdopt.getSaleNo());
        return savedAdopt;
    }

    @Transactional
    public void updateAdoptImageSaleNo(Integer[] imageNoArr, Integer saleNo) {
        if (!Objects.isNull(imageNoArr)) {
            // 분양 이미지 테이블의 saleNo 값을 분양 테이블의 saleNo 값으로 업데이트 해준다.
            for (Integer imageNo : imageNoArr) {
                adoptImageRepository.updateAdoptImageSaleNo(saleNo, imageNo);
            }
        }
    }

    @Transactional
    public AdoptBookmark insertAdoptBookmark(SecurityUserDto dto, Integer saleNo) {
        // 회원의 정보와 현재 분양 게시글의 정보를 가져온다.
        Adopt adopt = findBySaleNo(saleNo);
        Member member = memberService.findByMemberNo(dto.getMemberNo());
        // 외래키 업데이트를 위해 관심 분양 게시글 엔티티에 회원과 분양 게시글 엔티티를 셋팅해준다.
        AdoptBookmark adoptBookmark = new AdoptBookmark(dto.getEmail(), adopt, member);
        return adoptBookmarkRepository.save(adoptBookmark);
    }

    public AdoptResponseDto readAdopt(Integer saleNo) {
        // Adopt 테이블과 Member 테이블을 조인해서 가져올 수 있는 데이터를 먼저 채운다.
        AdoptResponseDto responseDto = queryService.selectAdoptAndMember(saleNo);
        // 현재 분양 게시글과 관련이 있는 이미지 url을 조회해온다.
        String[] images = queryService.selectAdoptImages(saleNo).toArray(String[]::new);
        // 현재 비어있는 responseDto의 이미지 필드의 값을 채워준다.
        responseDto.addImages(images);
        return responseDto;
    }


}
