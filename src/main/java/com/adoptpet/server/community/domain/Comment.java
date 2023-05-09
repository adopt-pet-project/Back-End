package com.adoptpet.server.community.domain;

import com.adoptpet.server.user.domain.Member;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "COMMENT")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_no")
    private Integer commentNo;

    @Column(name = "content")
    private String content;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Column(name = "mod_date")
    private LocalDateTime modDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> child = new ArrayList<>();

    @Column(name = "reg_id")
    private String regId;

    @Column(name = "mod_id")
    private String modId;

    @Enumerated(EnumType.STRING)
    @Column(name = "visible_yn")
    private VisibleYnEnum visibleYn;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "logical_del")
    private LogicalDelEnum logicalDel;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "blind_yn")
    private BlindYnEnum blindYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_no")
    private Community community;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    private Member member;

    @OneToMany(mappedBy = "comment")
    private List<CommentHeart> commentHearts = new ArrayList<>();

    //== 연관관계 메서드 ==//
    public void addCommunity(Community community){
        this.community = community;
        community.getComment(this);
    }

    public void addMember(Member member){
        this.member = member;
        //member.getComment() 만들기
    }

    public void getCommentHeart(CommentHeart commentHeart) {
        this.commentHearts.add(commentHeart);
    }

    //== 생성 메서드 ==//

    //== 조회 메서드 ==//

    //== 수정 메서드 ==//

    //== 비즈니스 로직 ==//


}
