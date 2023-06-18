package com.adoptpet.server.community.controller;

import com.adoptpet.server.commons.security.dto.SecurityUserDto;
import com.adoptpet.server.commons.support.StatusResponseDto;
import com.adoptpet.server.commons.util.SecurityUtils;
import com.adoptpet.server.community.dto.ArticleDetailInfoDto;
import com.adoptpet.server.community.dto.ArticleListDto;
import com.adoptpet.server.community.dto.ArticleDto;
import com.adoptpet.server.community.dto.CommentListDto;
import com.adoptpet.server.community.dto.request.*;
import com.adoptpet.server.community.dto.response.ArticleInfoResponse;
import com.adoptpet.server.community.dto.response.ArticleListResponse;
import com.adoptpet.server.community.dto.response.CommentListResponse;
import com.adoptpet.server.community.service.CommentService;
import com.adoptpet.server.community.service.CommunityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.adoptpet.server.commons.support.StatusResponseDto.*;
import static com.adoptpet.server.community.dto.request.HeartTargetEnum.ARTICLE;
import static com.adoptpet.server.community.dto.request.HeartTargetEnum.COMMENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community")
@Slf4j
public class CommunityController {

    private final CommunityService communityService;
    private final CommentService commentService;

    //== 응답 status 200 반환 ==//
    private static ResponseEntity<StatusResponseDto> success() {
        return ResponseEntity.ok(StatusResponseDto.success());
    }

    private static ResponseEntity<StatusResponseDto> success(Object responseData) {
        return ResponseEntity.ok(StatusResponseDto.success(responseData));
    }

    @PostMapping("/heart")
    public ResponseEntity<StatusResponseDto> heartRegistration(@Valid @RequestBody HeartRequest request){

        Integer like = null;
        HeartTargetEnum target = request.getTarget();
        final Integer targetNo = request.getTargetNo();

        if (target.equals(ARTICLE)){
            // 게시글 좋아요 등록
            like = communityService.insertArticleHeart(SecurityUtils.getUser(), targetNo);
        } else if(target.equals(COMMENT)) {
            // 댓글 좋아요 등록
            like = commentService.insertCommentHeart(SecurityUtils.getUser(), targetNo);
        }

        return success(like);
    }


    @DeleteMapping("/heart/{target}/{targetId}")
    public ResponseEntity<StatusResponseDto> heartDeletion(
            @PathVariable("target") String targetValue,
            @PathVariable("targetId") Integer targetNo){

        Integer like = null;
        HeartTargetEnum target = HeartTargetEnum.from(targetValue);

        if (target.equals(ARTICLE)){
            // 게시글 좋아요 삭제
            like = communityService.deleteArticleHeart(SecurityUtils.getUser(), targetNo);
        } else if(target.equals(COMMENT)) {
            // 댓글 좋아요 삭제
            like = commentService.deleteCommentHeart(SecurityUtils.getUser(), targetNo);
        }

        return success(like);
    }



    //== 댓글 조회 ==//
    @GetMapping("/comment/{articleId}")
    public ResponseEntity<List<CommentListResponse>> readCommentList(
            @PathVariable("articleId") @Min(value = 0) Integer articleNo,
            @RequestHeader(value = "Authorization",required = false) String accessToken
    ){
        List<CommentListDto> commentList
                = commentService.readCommentList(articleNo,accessToken);

        List<CommentListResponse> response = commentList.stream()
                .map(CommentListDto::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    //== 댓글 등록 ==//
    @PostMapping("/comment")
    public ResponseEntity<StatusResponseDto> commentRegistration(@Valid @RequestBody RegisterCommentRequest request){

        commentService.insertComment(request.getContent(), request.getParentNo(), request.getArticleNo());

        return success();
    }


    //== 댓글 수정 ==//
    @PatchMapping("/comment")
    public ResponseEntity<StatusResponseDto> commentModification(
            @Valid @RequestBody ModifyCommentRequest request, BindingResult bindingResult
    ){

        // 유효성 검증에 실패할경우 400번 에러를 응답한다.
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(addStatus(400));
        }

        commentService.updateComment(request.getCommentNo(), request.getContent());

        return success();
    }


    //== 댓글 삭제  ==//
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<StatusResponseDto> commentDeletion(
            @PathVariable("commentId") Integer commentNo) {

        commentService.deleteComment(commentNo);

        return success();
    }


    //== 게시글 목록 조회 ==//
    @GetMapping("/list/{order}")
    public ResponseEntity<ArticleListResponse> readArticleList(
            @PathVariable("order") String order,
            @RequestParam(value = "page",required = false) Integer pageNum,
            @RequestParam(value = "option", required = false) Integer option,
            @RequestParam(value = "keyword", required = false) String keyword){

        ArticleListResponse.ArticleListResponseBuilder responseBuilder = ArticleListResponse.builder();

        // 첫페이지를 조회할 경우 인기 게시글 조회
        if(Objects.isNull(pageNum) || pageNum == 1){
            // 인기글 조회
            Map<String, ArticleListDto>  trendingArticleMap = communityService.getTrendingArticleDayAndWeekly();
            // 응답 형태로 변환
            responseBuilder.hot(trendingArticleMap.get("hot"))
                    .weekly(trendingArticleMap.get("weekly"));
        }

        // 요청 리소스로 게시글 목록 조회
        List<ArticleListDto> articleList = communityService.readArticleList(order,pageNum,option,keyword);
        // 응답 형태로 변환 후 빌드
        ArticleListResponse response = responseBuilder.list(articleList).build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/article/{articleNo}")
    public ResponseEntity<ArticleInfoResponse> readArticle(
            @PathVariable("articleNo") Integer articleNo,
            @RequestHeader(value = "Authorization",required = false) String accessToken){

        ArticleDetailInfoDto articleDetailInfoDto =
                communityService.readArticle(articleNo,accessToken);

        return ResponseEntity.ok(articleDetailInfoDto.toResponse());
    }

    @PostMapping("/view")
    public ResponseEntity<StatusResponseDto> articleViewUpdate(
            @Valid @RequestBody UpdateViewRequest viewRequest,
            HttpServletRequest request, HttpServletResponse response){

        communityService.increaseCount(viewRequest.getArticleNo(), request, response);

        return success();
    }


    @PostMapping("/article")
    public ResponseEntity<StatusResponseDto> articleRegistration(
            @Valid @RequestBody RegisterArticleRequest request, BindingResult bindingResult){

        // 유효성 검증에 실패할경우 400번 에러를 응답한다.
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(addStatus(400));
        }

        // 현재 회원의 인증 객체를 가져온다.
        SecurityUserDto user = SecurityUtils.getUser();
        // 게시글 dto 생성
        ArticleDto articleDto = request.toDto(user.getEmail());
        // 게시글 등록
        communityService.insertArticle(articleDto);

        return success();
    }

    @PatchMapping("/article/{articleNo}")
    public ResponseEntity<StatusResponseDto> articleModification(
            @RequestBody @Valid UpdateArticleRequest request,
            @PathVariable("articleNo") Integer articleNo,BindingResult bindingResult){

        // 유효성 검증에 실패할경우 400번 에러를 응답한다.
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(addStatus(400));
        }

        // UpdateArticleRequest -> CommunityDto
        ArticleDto articleDto = request.toDto();
        // DB Update
        communityService.updateArticle(articleDto,articleNo);

        return success();
    }


    @DeleteMapping("/article/{articleNo}")
    public ResponseEntity<StatusResponseDto> articleDeletion(
            @PathVariable("articleNo") Integer articleNo) {

        communityService.softDeleteArticle(articleNo);

        return success();
    }

    @PostMapping("/bookmark/{articleNo}")
    public ResponseEntity<StatusResponseDto> articleBookmarkAddition(
            @PathVariable("articleNo") Integer articleNo){

        communityService.insertArticleBookmark(SecurityUtils.getUser(), articleNo);

        return success();
    }

    @DeleteMapping("/bookmark/{articleNo}")
    public ResponseEntity<StatusResponseDto> articleBookmarkDeletion(
            @PathVariable("articleNo") Integer articleNo){

        communityService.deleteArticleBookmark(SecurityUtils.getUser(), articleNo);

        return success();
    }
}
