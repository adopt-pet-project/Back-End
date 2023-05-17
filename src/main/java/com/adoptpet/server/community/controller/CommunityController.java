package com.adoptpet.server.community.controller;

import com.adoptpet.server.commons.security.dto.SecurityUserDto;
import com.adoptpet.server.commons.util.SecurityUtils;
import com.adoptpet.server.community.domain.BlindYnEnum;
import com.adoptpet.server.community.domain.LogicalDelEnum;
import com.adoptpet.server.community.dto.CommunityDto;
import com.adoptpet.server.community.dto.request.RegisterArticleRequest;
import com.adoptpet.server.community.service.CommunityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community")
@Slf4j
public class CommunityController {

    private final CommunityService communityService;

    @PostMapping
    public ResponseEntity<CommunityDto> writeArticle (@RequestBody @Valid RegisterArticleRequest request){

        // 현재 회원의 인증 객체를 가져온다.
        SecurityUserDto user = SecurityUtils.getUser();

        // 게시글 dto 생성
        CommunityDto communityDto = CommunityDto.builder()
                .categoryNo(request.getCategoryNo())
                .title(request.getTitle())
                .content(request.getContent())
                .regId(user.getEmail())
                .modId(user.getEmail())
                .viewCount(0)
                .visibleYn(request.getVisibleYn())
                .blindYn(BlindYnEnum.NORMAL)
                .logicalDel(LogicalDelEnum.NORMAL)
                .build();

        CommunityDto resultCommunity = communityService.insertArticle(communityDto);


        return ResponseEntity.ok(resultCommunity);
    }


}
