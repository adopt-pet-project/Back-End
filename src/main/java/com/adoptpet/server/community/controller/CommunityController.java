package com.adoptpet.server.community.controller;

import com.adoptpet.server.commons.security.dto.SecurityUserDto;
import com.adoptpet.server.commons.support.StatusResponseDto;
import com.adoptpet.server.commons.util.SecurityUtils;
import com.adoptpet.server.community.domain.Community;
import com.adoptpet.server.community.dto.ArticleDetailInfo;
import com.adoptpet.server.community.dto.CommunityDto;
import com.adoptpet.server.community.dto.request.RegisterArticleRequest;
import com.adoptpet.server.community.dto.request.RegisterCommentRequest;
import com.adoptpet.server.community.dto.request.UpdateArticleRequest;
import com.adoptpet.server.community.dto.response.ArticleInfoResponse;
import com.adoptpet.server.community.dto.response.ArticleListResponse;
import com.adoptpet.server.community.dto.response.CommentListResponse;
import com.adoptpet.server.community.service.CommentService;
import com.adoptpet.server.community.service.CommunityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.adoptpet.server.commons.support.StatusResponseDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community")
@Slf4j
public class CommunityController {

    private final CommunityService communityService;

    private final CommentService commentService;


//    @GetMapping("/comment/{boardId}")
//    public ResponseEntity<CommentListResponse> readCommentList(
//            @PathVariable("boardId") Integer articleNo){
//
//        communityService.readCommentList(articleNo);
//
//        return ResponseEntity.ok();
//    }

//    @PostMapping("/comment")
//    public ResponseEntity<StatusResponseDto> writeComment(@RequestBody RegisterCommentRequest request){
//
//    }


    @GetMapping("/list")
    public ResponseEntity<ArticleListResponse> readArticleList(){

        return ResponseEntity.ok().build();
    }

    @GetMapping("/article/{articleNo}")
    public ResponseEntity<ArticleInfoResponse> readArticle(
            @PathVariable("articleNo") Integer articleNo,
            @RequestHeader(value = "Authorization",required = false) String accessToken){

        ArticleDetailInfo articleDetailInfo = communityService.readArticle(articleNo,accessToken);

        return ResponseEntity.ok(articleDetailInfo.toResponse());
    }

    @PostMapping("/article")
    public ResponseEntity<StatusResponseDto> writeArticle (
            @RequestBody @Valid RegisterArticleRequest request, BindingResult bindingResult){

        // 유효성 검증에 실패할경우 400번 에러를 응답한다.
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(addStatus(400));
        }

        // 현재 회원의 인증 객체를 가져온다.
        SecurityUserDto user = SecurityUtils.getUser();
        // 게시글 dto 생성
        CommunityDto communityDto = request.toDto(user.getEmail());
        // 게시글 등록
        communityService.insertArticle(communityDto);

        return ResponseEntity.ok(success());
    }

    @PatchMapping("/article/{articleNo}")
    public ResponseEntity<StatusResponseDto> updateArticle(
            @RequestBody @Valid UpdateArticleRequest request,
            @PathVariable("articleNo") Integer articleNo,BindingResult bindingResult){

        // 유효성 검증에 실패할경우 400번 에러를 응답한다.
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(addStatus(400));
        }

        // UpdateArticleRequest -> CommunityDto
        CommunityDto communityDto = request.toDto();
        // DB Update
        communityService.updateArticle(communityDto,articleNo);

        return ResponseEntity.ok(success());
    }


    @DeleteMapping("/article/{articleNo}")
    public ResponseEntity<StatusResponseDto> deleteAdopt(
            @PathVariable("articleNo") Integer articleNo) {

        communityService.softDeleteArticle(articleNo);

        return ResponseEntity.ok(success());
    }


}
