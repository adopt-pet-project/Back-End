package com.adoptpet.server.community.service;

import com.adoptpet.server.commons.exception.CustomException;
import com.adoptpet.server.commons.exception.ErrorCode;
import com.adoptpet.server.commons.notification.domain.NotifiTypeEnum;
import com.adoptpet.server.commons.notification.service.NotificationService;
import com.adoptpet.server.commons.security.dto.SecurityUserDto;
import com.adoptpet.server.commons.util.SecurityUtils;
import com.adoptpet.server.community.domain.Comment;
import com.adoptpet.server.community.domain.CommentHeart;
import com.adoptpet.server.community.domain.Community;
import com.adoptpet.server.community.dto.CommentListDto;
import com.adoptpet.server.community.dto.CommentTypeEnum;
import com.adoptpet.server.community.repository.CommentHeartRepository;
import com.adoptpet.server.community.repository.CommentRepository;
import com.adoptpet.server.community.repository.CommunityRepository;
import com.adoptpet.server.user.domain.Member;
import com.adoptpet.server.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.adoptpet.server.commons.exception.ErrorCode.*;
import static com.adoptpet.server.commons.exception.ErrorCode.DUPLICATE_NOT_HEART;
import static com.adoptpet.server.community.dto.CommentTypeEnum.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommunityRepository communityRepository;
    private final MemberService memberService;
    private final CommentHeartRepository commentHeartRepository;
    private final NotificationService notificationService;


    @Transactional(readOnly = true)
    public Comment findCommentByNo(Integer commentNo) {
        return commentRepository.findById(commentNo)
                .orElseThrow(ErrorCode::throwCommentNotFound);
    }


    @Transactional(readOnly = true)
    public Community findArticleByNo(Integer articleNo) {
        return communityRepository.findById(articleNo)
                .orElseThrow(ErrorCode::throwArticleNotFound);
    }

    /**
     * @title 댓글 등록
     * @param content   : 댓글 내용
     * @param parentNo  : (대댓글 등록시) 부모 댓글 고유키
     * @param articleNo : 게시글 고유키
     **/
    @Transactional
    public void insertComment(String content, Integer parentNo, Integer articleNo) {

        final int loginMemberNo = SecurityUtils.getUser().getMemberNo();
        Member writer = memberService.findByMemberNo(loginMemberNo);
        Community community = findArticleByNo(articleNo);

        log.info("writerMember : {}", writer.getMemberNo());

        NotifiTypeEnum notiType = null;
        Member receiver = null;
        Integer refId = null;

        // 댓글 생성
        Comment comment = Comment.createComment(content, writer ,community);
        // 댓글-대댓글 구분
        if (Objects.nonNull(parentNo)) {
            // 요청에 포함된 부모 댓글 조회
            Comment parent = findCommentByNo(parentNo);
            // 대댓글 엔티티에 부모 댓글 엔티티 추가
            comment.addParent(parent);

            // 알림 데이터 입력
            notiType = NotifiTypeEnum.REPLY;// 대댓글이 달렸을 경우
            receiver = comment.getParent().getMember();
            refId = articleNo;
        } else {
            notiType = NotifiTypeEnum.COMMENT;// 댓글이 달렷을 경우
            receiver = memberService.findByEmail(community.getRegId())
                    .orElseThrow(ErrorCode::throwEmailNotFound);
            refId = articleNo;
        }
        Comment save = commentRepository.save(comment);

        // 글 작성자와 알림 받는 사람이 일치하지 않을 때 알림 전송
        if(!receiver.getMemberNo().equals(writer.getMemberNo())){
            // 알림 등록(알림받을 대상자, 알림 타입, 발생지 소유자 고유키, 알림 내용)
            notificationService.send(writer, receiver, notiType, refId, save.getContent());
        }
    }


    /**
     * @title 댓글 목록 조회
     * @param articleNo : 게시글 고유키
     **/
    @Transactional(readOnly = true)
    public List<CommentListDto> readCommentList(Integer articleNo, String accessToken) {
        List<CommentListDto> commentDtoList = new ArrayList<>();
        // 게시글 엔티티로 댓글 목록 조회
        List<Comment> comments = commentRepository.findByArticleNo(articleNo);
        // 댓글이 없을 경우
        if(comments.isEmpty()){
            commentDtoList = new ArrayList<>(0);
        } else {
            for(Comment comment : comments) {
                // 댓글의 자식 댓글이 있을 경우
                if( !comment.getChild().isEmpty()) {
                    // 대댓글을 DTO로 변환 후 List에 저장
                    List<CommentListDto> childDtoList = comment.getChild()
                            .stream()
                            .map(child -> convertToDto(child, REPLY, accessToken))
                            .collect(Collectors.toList());
                    // 댓글 DTO로 변환
                    CommentListDto parentDto = convertToDto(comment, COMMENT, accessToken);
                    // 대댓글 저장
                    parentDto.addChildCommentList(childDtoList);
                    // 반환할 DTO List에 추가
                    commentDtoList.add(parentDto);

                // 댓글의 부모 댓글이 없을 경우
                } else if(Objects.isNull(comment.getParent())) {
                    // 댓글을 DTO로 변환
                    CommentListDto commentDto = convertToDto(comment, COMMENT, accessToken);
                    // 반환할 DTO List에 추가
                    commentDtoList.add(commentDto);
                }
            }
        }
        return commentDtoList;
    }

    /**
    * @title 댓글 목록 조회
     * @param memberNo : 회원 고유키
    **/
    @Transactional(readOnly = true)
    public List<Comment> readCommentList(Integer memberNo){
        return commentRepository.findByMemberNo(memberNo);
    }


    /**
     * @title 댓글 수정
     * @param commentNo : 댓글/대댓글 교유키
     * @param content   : 수정할 댓글 내용
     **/
    @Transactional
    public void updateComment(Integer commentNo, String content) {
        final String commentModEmailId = SecurityUtils.getUserId();

        Comment comment = findCommentByNo(commentNo);

        // 댓글 정보 수정
        comment.updateComment(content, commentModEmailId);
        try {
            commentRepository.save(comment);
        } catch (RuntimeException ex) {
            log.error("comment update failed :: " + ex.getLocalizedMessage());
            throw new CustomException(UNSUCCESSFUL_MODIFY);
        }

    }


    /**
     * @title 댓글 삭제시 대댓글의 유무에 따른 분기 처리
     * 1. 대댓글이 있을 경우 논리 삭제
     * 2. 대댓글이 없을 경우 물리 삭데
     **/
    @Transactional
    public void deleteComment(Integer commentNo) {
        // 댓글 조회
        Comment comment = findCommentByNo(commentNo);

        try {
            // 대댓글 여부 확인
            if (!comment.getChild().isEmpty()) {
                // 대댓글이 있을 경우 논리 삭제
                comment.softDeleteComment();
                commentRepository.save(comment);
            } else {
                // 대댓글이 없을 경우 물리 삭제
                commentRepository.delete(comment);
            }

        } catch (RuntimeException ex) {
            log.error("comment delete failed :: " + ex.getLocalizedMessage());
            throw new CustomException(UNSUCCESSFUL_MODIFY);
        }
    }


    /**
    * @title 댓글 좋아요 저장
    **/
    @Transactional
    public Integer insertCommentHeart(SecurityUserDto userDto, Integer commentNo) {

        Member member = memberService.findByMemberNo(userDto.getMemberNo());
        Comment comment = findCommentByNo(commentNo);

        Optional<CommentHeart> findHeart
                = commentHeartRepository.findByCommentAndMember(comment, member);
        // 해당 멤버가 해당 댓글에 좋아요를 이미 눌렀는지 확인
        if (findHeart.isPresent()) {
            throw new CustomException(DUPLICATE_HEART);
        }

        CommentHeart heart = CommentHeart.createHeart(userDto.getEmail(), member, comment);
        commentHeartRepository.save(heart);

        // 업데이트된 좋아요 개수 반환
        Comment resultComment = findCommentByNo(commentNo);
        return resultComment.getHeartCnt();
    }

    /**
    * @title 댓글 좋아요 제거
    **/
    @Transactional
    public Integer deleteCommentHeart(SecurityUserDto dto, Integer commentNo) {

        Member member = memberService.findByMemberNo(dto.getMemberNo());
        Comment comment = findCommentByNo(commentNo);

        Optional<CommentHeart> findHeart = commentHeartRepository.findByCommentAndMember(comment, member);
        // 해당 멤버가 해당 댓글의 좋아요를 안가졌는지 확인
        if (findHeart.isEmpty()) {
            throw new CustomException(DUPLICATE_NOT_HEART);
        }
        commentHeartRepository.deleteByHeartNo(findHeart.get().getHeartNo());
        // 업데이트된 좋아요 개수 반환
        Comment resultComment = findCommentByNo(commentNo);
        return resultComment.getHeartCnt();
    }

    //== converter (entity to dto) ==//
    private static CommentListDto convertToDto(Comment comment, CommentTypeEnum type, String accessToken) {

        boolean mine = false;

        final int commentMemberNo = comment.getMember().getMemberNo();

        if(StringUtils.hasText(accessToken)){
            Integer memberNo = SecurityUtils.getUser().getMemberNo();
            if(memberNo.equals(commentMemberNo)){
                mine = true;
            }
        }

        return CommentListDto.builder()
                .type(type)
                .mine(mine)
                .commentNo(comment.getCommentNo())
                .nickname(comment.getMember().getNickname())
                .memberNo(comment.getMember().getMemberNo())
                .profile(comment.getMember().getProfile())
                .content(comment.getContent())
                .regDate(comment.getRegDate())
                .commentHeart(comment.getCommentHearts().size())
                .logicalDel(comment.getLogicalDel())
                .blindYn(comment.getBlindYn())
                .build();
    }
}
