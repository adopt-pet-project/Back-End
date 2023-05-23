package com.adoptpet.server.community.service;

import com.adoptpet.server.commons.exception.ErrorCode;
import com.adoptpet.server.commons.util.SecurityUtils;
import com.adoptpet.server.community.domain.Comment;
import com.adoptpet.server.community.domain.Community;
import com.adoptpet.server.community.dto.CommentDto;
import com.adoptpet.server.community.repository.CommentRepository;
import com.adoptpet.server.community.repository.CommunityRepository;
import com.adoptpet.server.community.service.mapper.CreateCommentMapper;
import com.adoptpet.server.user.domain.Member;
import com.adoptpet.server.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void insertComment(CommentDto commentDto, Integer articleNo){

        Member member = findMemberByEmail(SecurityUtils.getUserId());

        final CreateCommentMapper createComment = CreateCommentMapper.INSTANCE;

        Comment comment = createComment.toEntity(commentDto);
        comment.addMember(member);

        if(commentDto.getParentNo() != null) {
            Comment parent = findCommentByNo(commentDto.getParentNo());
            comment.addParent(parent);
        }

    }
}
