package com.adoptpet.server.community.service;

import com.adoptpet.server.commons.exception.CustomException;
import com.adoptpet.server.commons.exception.ErrorCode;
import com.adoptpet.server.commons.util.SecurityUtils;
import com.adoptpet.server.community.domain.Comment;
import com.adoptpet.server.community.domain.Community;
import com.adoptpet.server.community.repository.CommentRepository;
import com.adoptpet.server.community.repository.CommunityRepository;
import com.adoptpet.server.user.domain.Member;
import com.adoptpet.server.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;

    private final MemberRepository memberRepository;

    private final CommunityRepository communityRepository;


    @Transactional
    public Comment findCommentByNo(Integer commentNo){
        return commentRepository.findById(commentNo)
                .orElseThrow(ErrorCode::throwCommentNotFound);
    }

    private Member findMemberByEmail(String email){
        return memberRepository.findByEmail(email)
                .orElseThrow(ErrorCode::throwEmailNotFound);
    }

    private Community findArticleByNo(Integer articleNo){
        return communityRepository.findById(articleNo)
                .orElseThrow(ErrorCode::throwArticleNotFound);
    }

    @Transactional
    public void insertComment(String content,Integer parentNo,Integer articleNo){
        // security 이메일로 회원 조회
        String userEmail = SecurityUtils.getUserId();
        Member member = findMemberByEmail(userEmail);
        // 게시글 고유키로 게시글 엔티티 조회
        Community community = findArticleByNo(articleNo);
        Comment comment = Comment.createComment(content, userEmail);
        // 댓글 엔티티에 회원 엔티티 추가
        comment.addMember(member);
        comment.addCommunity(community);
        // 댓글-대댓글 구분
        if(Objects.nonNull(parentNo)) {
            // 요청에 포함된 부모 댓글 조회
            Comment parent = findCommentByNo(parentNo);
            // 대댓글 엔티티에 부모 댓글 엔티티 추가
            comment.addParent(parent);
        }
        try{
            // 댓글 저장
            commentRepository.save(comment);
        } catch (RuntimeException ex){
            log.error("comment insert failed :: "+ ex.getLocalizedMessage());
            throw new CustomException(ErrorCode.UNSUCCESSFUL_COMMENT);
        }
    }
}
