package com.adoptpet.server.community.service;

import com.adoptpet.server.adopt.repository.MemberRepository;
import com.adoptpet.server.community.domain.Category;
import com.adoptpet.server.community.domain.Community;
import com.adoptpet.server.community.dto.CommunityDto;
import com.adoptpet.server.community.mapper.CommunityMapper;
import com.adoptpet.server.community.repository.CategoryRepository;
import com.adoptpet.server.community.repository.CommunityRepository;
import com.adoptpet.server.user.domain.Member;
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


    public CommunityDto createArticle(CommunityDto communityDto, String memberEmail, Integer categoryNo){

        final CommunityMapper communityMapper = CommunityMapper.INSTANCE;

        Member findMember = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member email"));

        Category findCategory = categoryRepository.findById(categoryNo)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category number"));

        Community community = communityMapper.toEntity(communityDto);

        community.addMember(findMember);
        community.addCategory(findCategory);

        Community saveArticle = communityRepository.save(community);

        return communityMapper.toDTO(saveArticle);
    }
}
