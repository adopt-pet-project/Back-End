package com.adoptpet.server.user.service;

import com.adoptpet.server.adopt.dto.response.MyAdoptResponseDto;
import com.adoptpet.server.adopt.service.AdoptQueryService;
import com.adoptpet.server.commons.security.dto.SecurityUserDto;
import com.adoptpet.server.community.domain.Comment;
import com.adoptpet.server.community.domain.Community;
import com.adoptpet.server.community.dto.ArticleListDto;
import com.adoptpet.server.community.service.CommentService;
import com.adoptpet.server.community.service.CommunityService;
import com.adoptpet.server.user.domain.Member;
import com.adoptpet.server.user.dto.response.MyArticleResponseDto;
import com.adoptpet.server.user.dto.response.MyCommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MyAdoptService {

    private final AdoptQueryService adoptQueryService;
    private final CommunityService communityService;
    private final CommentService commentService;
    private final MemberService memberService;


    private static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy. MM. dd HH:mm");


    //== 회원 조회 ==//
    private Member getMember(SecurityUserDto loginMember) {
        return memberService.findByMemberNo(loginMember.getMemberNo());
    }


    /**
     * @title 회원 게시글 목록
     * @param loginMember : 로그인한 회원 정보(by Security)
     */
    @Transactional(readOnly = true)
    public List<MyArticleResponseDto> myArticleList(SecurityUserDto loginMember) {
        Member member = getMember(loginMember);
        List<ArticleListDto> articleList = communityService.readArticleList(member.getEmail());
        // dto to responseDto
        return articleList.stream()
                .map(MyAdoptService::toMyArticleDto)
                .collect(Collectors.toList());
    }

    private static MyArticleResponseDto toMyArticleDto(ArticleListDto dto){
        return MyArticleResponseDto.builder()
                .id(dto.getArticleNo())
                .title(dto.getTitle())
                .contents(dto.getContent())
                .publishedAt(dto.getRegDate().format(DATE_FORMAT))
                .thumbnail(dto.getThumbnail())
                .views(dto.getViewCount())
                .comment(dto.getCommentCnt())
                .like(dto.getLikeCnt())
                .build();
    }

    /**
     * @title 회원 댓글 목록
     * @param loginMember : 로그인한 회원 정보(by Security)
     **/
    @Transactional(readOnly = true)
    public List<MyCommentResponseDto> myCommentList(SecurityUserDto loginMember){
        Member member = getMember(loginMember);
        List<Comment> comments = commentService.readCommentList(member.getMemberNo());

        // entity to dto
        return comments.stream()
                .map(MyAdoptService::toMyCommentDto)
                .collect(Collectors.toList());
    }


    private static MyCommentResponseDto toMyCommentDto(Comment comment){
        Community article = comment.getCommunity();

        return MyCommentResponseDto.builder()
                .commentNo(comment.getCommentNo())
                .commentContent(comment.getContent())
                .articleNo(article.getArticleNo())
                .articleContent(article.getContent())
                .publishedAt(comment.getRegDate().format(DATE_FORMAT))
                .build();
    }



    public List<MyAdoptResponseDto> myAdoptList(String status, SecurityUserDto userDto) {
        if (status.equals("interested")) {
            List<Integer> keys = adoptQueryService.myInterestedAdoptKeys(userDto);
            return adoptQueryService.myInterestedAdoptList(keys);
        }

        return adoptQueryService.myAdoptList(status, userDto);
    }
}
