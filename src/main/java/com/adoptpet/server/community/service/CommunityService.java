package com.adoptpet.server.community.service;

import com.adoptpet.server.community.domain.Community;
import com.adoptpet.server.community.dto.ArticleDetailInfo;
import com.adoptpet.server.community.dto.CommunityDto;
import com.adoptpet.server.community.repository.CommunityQDslRepository;
import com.adoptpet.server.community.service.mapper.CreateArticleMapper;
import com.adoptpet.server.community.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final CommunityQDslRepository communityQDslRepository;
    @Transactional
    public void readArticle(Integer articleNo){
        ArticleDetailInfo articleDetail = communityQDslRepository.findArticleDetail(articleNo);
    }


    /**
    * 게시글 생성 메서드
     *  @ param CommunityDto : 게시글 생성 정보
     *  @ param String memberEmail : 작성한 유저 이메일
     *  @ param Integer category : 게시글이 속한 카테고리
     *  @ return CommunityDto : 생성 완료된 게시글 정보
    **/
    public CommunityDto insertArticle(CommunityDto communityDto){

        // CreateArticleMapper 인스턴스 생성
        final CreateArticleMapper createArticleMapper = CreateArticleMapper.INSTANCE;

        // DTO를 Entity로 매핑
        Community community = createArticleMapper.toEntity(communityDto);

        // Community 저장
        Community saveArticle = communityRepository.save(community);

        return createArticleMapper.toDTO(saveArticle);// 저장된 Community를 DTO로 변환하여 반환
    }
}
