package com.adoptpet.server.community.service;

import com.adoptpet.server.community.domain.Community;
import com.adoptpet.server.community.dto.ArticleDetailInfo;
import com.adoptpet.server.community.dto.CommunityDto;
import com.adoptpet.server.community.repository.CommunityImageRepository;
import com.adoptpet.server.community.repository.CommunityQDslRepository;
import com.adoptpet.server.community.service.mapper.CreateArticleMapper;
import com.adoptpet.server.community.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final CommunityQDslRepository communityQDslRepository;
    private final CommunityImageRepository communityImageRepository;

    /**
    * 게시글 상세 내용 조회
    **/
    @Transactional(readOnly = true)
    public ArticleDetailInfo readArticle(Integer articleNo){
        // 게시글 고유키로 게시글 조회
        Optional<Community> article = communityRepository.findById(articleNo);
        // 1.게시글이 존재할 경우
        if(article.isPresent()){
            // 게시글 정보 조회
            ArticleDetailInfo articleDetail = communityQDslRepository.findArticleDetail(articleNo);
            // 이미지 URL 조회 -> 없을 시 null 반환
            List<String> images = communityImageRepository.findImageUrlByArticleNo(articleNo)
                    .orElse(null);
            // 이미지 URL 리스트 추가
            articleDetail.addImages(images);
            // 조회된 게시글 정보 반환
            return articleDetail;
        }
        // 2.게시글이 존재하지 않을 경우
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Not Found article");
    }


    /**
    * 게시글 등록 메서드
     *  @param communityDto  : 게시글 생성 정보
     *  @return response     : 생성 완료된 게시글 정보
    **/
    @Transactional
    public CommunityDto insertArticle(CommunityDto communityDto){
        log.info("Thumbnail:{}",communityDto.getThumbnail());
        // CreateArticleMapper 인스턴스 생성
        final CreateArticleMapper createArticleMapper = CreateArticleMapper.INSTANCE;
        // DTO를 Entity로 매핑
        Community community = createArticleMapper.toEntity(communityDto);
        // Community 저장
        Community saveArticle = communityRepository.save(community);
        // 이미지 업데이트
        Integer[] imgNo = communityDto.getImgNo();
        updateImageByArticleNo(communityDto.getImgNo(), saveArticle.getArticleNo());
        // 저장된 Community를 DTO로 변환
        CommunityDto response = createArticleMapper.toDTO(saveArticle);
        // 반환값에 null 대신 imgNo 추가
        response.addImgNo(imgNo);
        return response;
    }


    @Transactional
    public void updateImageByArticleNo(Integer[] imageNoArr, Integer saleNo) {
        if (!Objects.isNull(imageNoArr)) {
            // 커뮤니티 이미지 테이블에 커뮤니티 게시글 고유키와 정렬 기준을 업데이트
            for (int i=0; i<imageNoArr.length; i++) {
                communityImageRepository.updateImagByArticleNo(saleNo, imageNoArr[i], (i+1));
            }
        }
    }
}
