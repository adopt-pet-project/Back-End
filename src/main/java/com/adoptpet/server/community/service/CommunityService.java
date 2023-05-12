package com.adoptpet.server.community.service;

import com.adoptpet.server.community.domain.Category;
import com.adoptpet.server.community.domain.Community;
import com.adoptpet.server.community.dto.CommunityDto;
import com.adoptpet.server.community.mapper.CreateArticleMapper;
import com.adoptpet.server.community.repository.CategoryRepository;
import com.adoptpet.server.community.repository.CommunityRepository;
import com.adoptpet.server.user.domain.Member;
import com.adoptpet.server.user.repository.MemberRepository;
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
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;

    /**
    * 게시글 생성 메서드
     *  @ param CommunityDto : 게시글 생성 정보
     *  @ param String memberEmail : 작성한 유저 이메일
     *  @ param Integer category : 게시글이 속한 카테고리
     *  @ return CommunityDto : 생성 완료된 게시글 정보
    **/
    public CommunityDto createArticle(CommunityDto communityDto, String memberEmail, Integer categoryNo){

        // CreateArticleMapper 인스턴스 생성
        final CreateArticleMapper createArticleMapper = CreateArticleMapper.INSTANCE;

        // 주어진 이메일로 Member를 찾아옴. 없으면 예외 발생
        Member findMember = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member email"));

        // 주어진 이메일로 Member를 찾아옴. 없으면 예외 발생
        Category findCategory = categoryRepository.findById(categoryNo)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category number"));

        Community community = createArticleMapper.toEntity(communityDto);// DTO를 Entity로 매핑

        community.addMember(findMember);
        community.addCategory(findCategory);

        Community saveArticle = communityRepository.save(community); // Community 저장

        return createArticleMapper.toDTO(saveArticle);// 저장된 Community를 DTO로 변환하여 반환
    }
}
