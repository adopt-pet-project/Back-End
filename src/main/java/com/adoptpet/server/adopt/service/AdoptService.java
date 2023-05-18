package com.adoptpet.server.adopt.service;

import com.adoptpet.server.adopt.domain.Adopt;
import com.adoptpet.server.adopt.domain.AdoptBookmark;
import com.adoptpet.server.adopt.dto.request.AdoptImageRequestDto;
import com.adoptpet.server.adopt.dto.request.AdoptRequestDto;
import com.adoptpet.server.adopt.dto.response.AdoptDetailResponseDto;
import com.adoptpet.server.adopt.repository.AdoptBookmarkRepository;
import com.adoptpet.server.adopt.repository.AdoptImageRepository;
import com.adoptpet.server.adopt.repository.AdoptRepository;
import com.adoptpet.server.commons.security.dto.SecurityUserDto;
import com.adoptpet.server.user.domain.Member;
import com.adoptpet.server.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    public AdoptDetailResponseDto readAdopt(Integer saleNo) {
        // Adopt 테이블과 Member 테이블을 조인해서 가져올 수 있는 데이터를 먼저 채운다.
        AdoptDetailResponseDto responseDto = queryService.selectAdoptAndMember(saleNo);
        // 현재 분양 게시글과 관련이 있는 이미지 url을 조회해온다.
        String[] images = queryService.selectAdoptImages(saleNo).toArray(String[]::new);
        // 현재 비어있는 responseDto의 이미지 필드의 값을 채워준다.
        responseDto.addImages(images);
        return responseDto;
    }


    private Adopt getAdopt(AdoptRequestDto adoptDto, SecurityUserDto user) {
        // AdoptRequestDto => Adopt Entity로 변환해서 정보를 저장한다.
        Adopt adopt = adoptDto.toEntity();

        if (Objects.nonNull(adoptDto.getImage())) {
            adopt.addThumbnail(adoptDto.getImage()[0].getImgUrl());
        }

        // 등록자 ID와 수정자 ID를 넣어준다.
        adopt.addRegIdAndModId(user.getEmail(), user.getEmail());
        return adopt;
    }

}
