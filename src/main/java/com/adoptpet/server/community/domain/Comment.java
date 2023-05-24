package com.adoptpet.server.community.domain;

import com.adoptpet.server.commons.support.BaseTimeEntity;
import com.adoptpet.server.user.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "COMMENT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_no")
    private Integer commentNo;

    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_no")
    private Comment parent;

    @OneToMany(mappedBy = "parent",orphanRemoval = true)
    private List<Comment> child = new ArrayList<>();

    @Column(name = "reg_id")
    private String regId;

    @Column(name = "mod_id")
    private String modId;

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

    @Builder
    public Comment(String content, Comment parent, String regId, String modId, LogicalDelEnum logicalDel, BlindYnEnum blindYn) {
        this.content = content;
        this.parent = parent;
        this.regId = regId;
        this.modId = modId;
        this.logicalDel = logicalDel;
        this.blindYn = blindYn;
    }

    public void addMember(Member member) {
        this.member = member;
    }

    public void addCommunity(Community community) {
        this.community = community;
    }

    public void addParent(Comment parent){
        this.parent = parent;
    }

    public static Comment createComment(String content, String email){
       return Comment.builder()
               .content(content)
               .regId(email)
               .modId(email)
               .logicalDel(LogicalDelEnum.NORMAL)
               .blindYn(BlindYnEnum.NORMAL)
               .build();
    }
}
