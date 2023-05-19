package com.adoptpet.server.community.controller;

import com.adoptpet.server.commons.security.dto.SecurityUserDto;
import com.adoptpet.server.commons.util.SecurityUtils;
import com.adoptpet.server.community.domain.Community;
import com.adoptpet.server.community.dto.ArticleDetailInfo;
import com.adoptpet.server.community.dto.CommunityDto;
import com.adoptpet.server.community.dto.request.RegisterArticleRequest;
import com.adoptpet.server.community.dto.request.UpdateArticleRequest;
import com.adoptpet.server.community.dto.response.ArticleInfoResponse;
import com.adoptpet.server.community.service.CommunityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community")
@Slf4j
public class CommunityController {

    private final CommunityService communityService;

    @GetMapping("/article/{articleNo}")
    public ResponseEntity<ArticleInfoResponse> readArticle(
            @PathVariable("articleNo") Integer articleNo){
        ArticleDetailInfo articleDetailInfo = communityService.readArticle(articleNo);
        return ResponseEntity.ok(articleDetailInfo.toResponse());
    }

    @PostMapping("/article")
    public ResponseEntity<CommunityDto> writeArticle (
            @RequestBody @Valid RegisterArticleRequest request){

        // 현재 회원의 인증 객체를 가져온다.
        SecurityUserDto user = SecurityUtils.getUser();
        // 게시글 dto 생성
        CommunityDto communityDto = request.toDto(user.getEmail());
        // 게시글 등록
        CommunityDto resultCommunity = communityService.insertArticle(communityDto);

        return ResponseEntity.ok(resultCommunity);
    }

    @PatchMapping("/article/{articleNo}")
    public ResponseEntity<Void> updateArticle(
            @RequestBody @Valid UpdateArticleRequest request, @PathVariable("articleNo") Integer articleNo){

        // 현재 회원의 인증 객체를 가져온다.
        SecurityUserDto user = SecurityUtils.getUser();

        CommunityDto communityDto = request.toDto();

        communityService.updateArticle(communityDto,user.getEmail(),articleNo);

        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/article/{articleNo}")
    public ResponseEntity<Community> deleteAdopt(@PathVariable("articleNo") Integer articleNo) {
        Community community = communityService.softDeleteArticle(articleNo);
        return ResponseEntity.ok(community);
    }

}
