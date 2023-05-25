package com.adoptpet.server.community.service;

import com.adoptpet.server.commons.exception.CustomException;
import com.adoptpet.server.commons.exception.ErrorCode;
import com.adoptpet.server.commons.util.SecurityUtils;
import com.adoptpet.server.community.domain.Comment;
import com.adoptpet.server.community.domain.Community;
import com.adoptpet.server.community.dto.CommentListDto;
import com.adoptpet.server.community.repository.CommentRepository;
import com.adoptpet.server.community.repository.CommunityRepository;
import com.adoptpet.server.user.domain.Member;
import com.adoptpet.server.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;

    private final MemberRepository memberRepository;

    private final CommunityRepository communityRepository;


    @Transactional(readOnly = true)
    public Comment findCommentByNo(Integer commentNo){
        return commentRepository.findById(commentNo)
                .orElseThrow(ErrorCode::throwCommentNotFound);
    }

    @Transactional(readOnly = true)
    public Member findMemberByEmail(String email){
        return memberRepository.findByEmail(email)
                .orElseThrow(ErrorCode::throwEmailNotFound);
    }

    @Transactional(readOnly = true)
    public Community findArticleByNo(Integer articleNo){
        return communityRepository.findById(articleNo)
                .orElseThrow(ErrorCode::throwArticleNotFound);
    }

    /**
    * 댓글 등록
     * @param content   : 댓글 내용
     * @param parentNo  : (대댓글일 경우) 부모 댓글 고유키
     * @param articleNo : 게시글 고유키
    **/
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
            throw new CustomException(ErrorCode.UNSUCCESSFUL_INSERT);
        }
    }


    /**
    * 댓글 목록 조회
     * @param articleNo : 게시글 고유키
    **/
    @Transactional(readOnly = true)
    public List<CommentListDto> readCommentList(Integer articleNo) {

        // 게시글 고유키 검증
        Community community = findArticleByNo(articleNo);
        // 게시글 엔티티로 댓글 조회
        List<Comment> comments = community.getComments();

        List<CommentListDto> commentList = new ArrayList<>();
        List<CommentListDto> childListDto = new ArrayList<>();

        for(Comment comment : comments){
            // 댓글 Entity to DTO
            CommentListDto commentData = commentToDto(comment,"comment");
            // 댓글의 대댓글이 있을 경우
            if(!comment.getChild().isEmpty()){
                // 대댓글 조회
                List<Comment> child = comment.getChild();
                // 대댓글 Entity to DTO
                child.stream()
                        .map(ch -> commentToDto(ch,"reply"))
                        .forEach(childListDto::add);
                // 댓글 DTO에 대댓글 데이터 추가
                commentData.addChildCommentList(childListDto);
            }
            // List에 댓글 DTO 추가
            commentList.add(commentData);
        }

        return commentList;
    }

    /**
    * 댓글 수정
     * @param commentNo : 댓글/대댓글 교유키
     * @param content   : 수정할 댓글 내용
    **/
    @Transactional
    public void updateComment(Integer commentNo, String content) {

        // 댓글 조회
        Comment comment = findCommentByNo(commentNo);
        final String commentModEmailId = SecurityUtils.getUserId();
        // 댓글 권한 체크
        compareRegIdAndModId(comment.getRegId(),commentModEmailId);
        // 수정 내용 적용
        comment.updateComment(content,commentModEmailId);

        try{
            // 댓글 update 요청
            commentRepository.save(comment);
        } catch (RuntimeException ex){
            log.error("comment update failed :: "+ ex.getLocalizedMessage());
            throw new CustomException(ErrorCode.UNSUCCESSFUL_MODIFY);
        }

    }


    /**
    * 댓글 삭제
     * - 삭제를 요청한 댓글의 대댓글 유무에 따른 분기 처리
     *   1. 대댓글이 있을 경우 논리 삭제
     *   2. 대댓글이 없을 경우 물리 삭데
    **/
    @Transactional
    public void deleteComment(Integer commentNo) {
        // 댓글 조회
        Comment comment = findCommentByNo(commentNo);
        // 댓글 권한 체크
        compareRegIdAndModId(comment.getRegId(),SecurityUtils.getUserId());
        try {
            // 대댓글 여부 확인
            if(!comment.getChild().isEmpty()){
                // 대댓글이 있을 경우 논리 삭제
                comment.softDeleteComment();
                commentRepository.save(comment);
            } else {
                // 대댓글이 없을 경우 물리 삭제
                commentRepository.delete(comment);
            }

        } catch (RuntimeException ex){
            log.error("comment delete failed :: "+ ex.getLocalizedMessage());
            throw new CustomException(ErrorCode.UNSUCCESSFUL_MODIFY);
        }
    }


    //== 작성자와 수정자 비교 메서드 ==//
    private static void compareRegIdAndModId(String regEmailId,String accessEmailId) {
        if(!regEmailId.equals(accessEmailId)){
            throw new CustomException(ErrorCode.DUPLICATE_EQUAL_REG_USER);
        }
    }

    //== converter ==//
    private static CommentListDto commentToDto(Comment comment, String type) {
        return CommentListDto.builder()
                .type(type)
                .commentNo(comment.getCommentNo())
                .nickname(comment.getMember().getNickname())
                .memberId(comment.getMember().getMemberNo())
                .content(comment.getContent())
                .regDate(comment.getRegDate())
                .commentHeart(comment.getCommentHearts().size())
                .type(type)
                .build();
    }

}
