package com.adoptpet.server.community.service;

import com.adoptpet.server.community.domain.Community;
import com.adoptpet.server.community.domain.LogicalDelEnum;
import com.adoptpet.server.community.dto.ArticleDetailInfo;
import com.adoptpet.server.community.dto.ArticleImageDto;
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

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final CommunityQDslRepository communityQDslRepository;
    private final CommunityImageRepository communityImageRepository;



    /**
    * 게시글 수정
    **/
    public Community updateArticle(CommunityDto communityDto, String modId, Integer articleNo){

        // 게시글 고유키로 게시글 검증
        Community community = findByArticleNo(articleNo);
        // 게시글 데이터 엔티티 저장
        community.updateArticleByMod(communityDto, modId);
        // 이미지 업데이트
        updateImageByArticleNo(communityDto.getImage(),articleNo);
        // 게시글 업데이트
        Community updatedArticle = communityRepository.save(community);

        return updatedArticle;
    }


    /**
    * 게시글 상세 내용 조회
    **/
    @Transactional(readOnly = true)
    public ArticleDetailInfo readArticle(Integer articleNo){
        // 게시글 고유키로 게시글 검증
        findByArticleNo(articleNo);
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


    /**
    * 게시글 등록 메서드
     *  @param communityDto  : 게시글 생성 정보
     *  @return response     : 생성 완료된 게시글 정보
    **/
    @Transactional
    public CommunityDto insertArticle(CommunityDto communityDto){
        // CreateArticleMapper 인스턴스 생성
        final CreateArticleMapper createArticleMapper = CreateArticleMapper.INSTANCE;
        // DTO를 Entity로 매핑
        Community community = createArticleMapper.toEntity(communityDto);
        // Community 저장
        Community saveArticle = communityRepository.save(community);
        // 이미지 업데이트
        ArticleImageDto[] images = communityDto.getImage();
        updateImageByArticleNo(communityDto.getImage(), saveArticle.getArticleNo());
        // 저장된 Community를 DTO로 변환
        CommunityDto response = createArticleMapper.toDTO(saveArticle);
        // 반환값에 null 대신 image 기입
        response.addImgNo(images);
        return response;
    }

    @Transactional
    public void updateImageByArticleNo(ArticleImageDto[] imageNoArr, Integer articleNo) {
        if (!Objects.isNull(imageNoArr)) {
            // 커뮤니티 이미지 테이블에 커뮤니티 게시글 고유키와 정렬 기준을 업데이트
            for (int i=0; i<imageNoArr.length; i++) {
                communityImageRepository.updateImagByArticleNo(articleNo, imageNoArr[i].getImageNo(), (i+1));
            }
        } else {
            communityImageRepository.updateAllByArticleNo(articleNo);
        }
    }
    
    
    /**
     * 게시글 논리 삭제
     */
    @Transactional
    public Community softDeleteArticle(Integer articleNo) {

        // 게시글 번호를 사용하여 해당 게시글을 조회한다.
        Community community = findByArticleNo(articleNo);
        // 게시글을 논리 삭제한다.
        community.deleteByLogicalDel(LogicalDelEnum.DELETE);
        // 게시글과 관련된 데이터인 북마크와 좋아요를 삭제한다.
        communityQDslRepository.deleteBookmark(community);
        communityQDslRepository.deleteArticleLike(community);
        // 게시글 이미지의 게시글 번호 컬럼을 비운다.(batch를 통해 자정에 삭제)
        communityImageRepository.updateAllByArticleNo(articleNo);
        // 변경된 게시글을 DB에 저장하여 업데이트한다.
        Community deletedCommunity = communityRepository.save(community);

        return deletedCommunity;
    }


    private Community findByArticleNo(Integer articleNo){
        // 게시글 번호로 게시글을 조회하고, 조회되지 않을 경우 예외를 발생시킨다.
        return communityRepository.findById(articleNo)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Not Found article"));
    }


}
