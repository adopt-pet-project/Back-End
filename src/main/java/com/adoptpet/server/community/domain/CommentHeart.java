package com.adoptpet.server.community.domain;

import com.adoptpet.server.user.domain.Member;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Table(name = "COMMENT_HEART")
public class CommentHeart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "heart_no")
    private Integer heartNo;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Column(name = "mod_date")
    private LocalDateTime modDate;

    @Column(name = "reg_id")
    private String regId;

    @Column(name = "mod_id")
    private String modId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_no")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "comment_no")
    private Comment comment;

    //== 연관관계 메서드 ==//
    public void addComment(Comment comment){
        this.comment = comment;
        comment.getCommentHeart(this);
    }

    public void addMember(Member member) {
        this.member = member;
        //member.getCommentHeart() 추가하기
    }

    //== 생성 메서드 ==//

    //== 조회 메서드 ==//

    //== 수정 메서드 ==//

    //== 비즈니스 로직 ==//

}