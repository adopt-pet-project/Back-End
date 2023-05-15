package com.adoptpet.server.adopt.service;

import com.adoptpet.server.adopt.domain.Adopt;
import com.adoptpet.server.adopt.domain.AdoptBookmark;
import com.adoptpet.server.adopt.dto.response.AdoptResponseDto;
import com.adoptpet.server.adopt.repository.AdoptBookmarkRepository;
import com.adoptpet.server.adopt.repository.AdoptImageRepository;
import com.adoptpet.server.adopt.repository.AdoptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdoptService {
    private final AdoptRepository adoptRepository;
    private final AdoptImageRepository adoptImageRepository;
    private final AdoptBookmarkRepository adoptBookmarkRepository;
    private final AdoptQueryService queryService;
    private final AdoptBookmarkRepository bookmarkRepository;

    public Adopt findBySaleNo(Integer saleNo) {
        return adoptRepository.findById(saleNo)
                .orElseThrow(IllegalStateException::new);
    }

    @Transactional
    public Adopt insertAdopt(Adopt adopt) {
        return adoptRepository.save(adopt);
    }

    @Transactional
    public void updateAdoptImageSaleNo(Integer[] imageNoArr, Integer saleNo) {
        // 분양 이미지 테이블의 saleNo 값을 분양 테이블의 saleNo 값으로 업데이트 해준다.
        for (Integer imageNo : imageNoArr) {
            adoptImageRepository.updateAdoptImageSaleNo(saleNo, imageNo);
        }
    }

    @Transactional
    public AdoptBookmark insertAdoptBookmark(AdoptBookmark bookmark) {
        return adoptBookmarkRepository.save(bookmark);
    }

    public AdoptResponseDto readAdopt(Integer saleNo) {
        // Adopt 테이블과 Member 테이블을 조인해서 가져올 수 있는 데이터를 먼저 채운다.
        AdoptResponseDto responseDto = queryService.selectAdoptAndMember(saleNo);
        // 현재 분양 게시글과 관련이 있는 이미지 url을 조회해온다.
        String[] images = queryService.selectAdoptImages(saleNo).toArray(String[]::new);
        // 현재 비어있는 responseDto의 이미지 필드의 값을 채워준다.
        responseDto.addImages(images);
        // 현재 분양 게시글과 관련이 있는 관심 카운트와 채팅 카운트를 조회한다.
        long bookmarkCount = bookmarkRepository.countAdoptBookmark(saleNo);
        // TODO: 채팅 구현시 수정 현재 1개로 하드코딩
        long chatCount = 1;
        // 관심 카운트와 채팅 카운트를 모두 넣어준다.
        responseDto.getContext().addCount(bookmarkCount,chatCount);
        return responseDto;
    }


}
