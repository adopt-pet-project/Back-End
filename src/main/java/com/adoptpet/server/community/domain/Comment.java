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

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "reg_date", nullable = false)
    private LocalDateTime regDate;

    @Column(name = "mod_date", nullable = false)
    private LocalDateTime modDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> child = new ArrayList<>();

    @Column(name = "reg_id", nullable = false)
    private String regId;

    @Column(name = "mod_id", nullable = false)
    private String modId;

    @Column(name = "visible_yn", nullable = false, columnDefinition = "varchar(10) default 'Y'")
    private String visibleYn = "Y";

    @Column(name = "logical_del", nullable = false, columnDefinition = "int default 0")
    private Integer logicalDel = 0;

    @Column(name = "blind_yn", nullable = false, columnDefinition = "int default 0")
    private Integer blindYn = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_no", nullable = false)
    private Community community;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no", nullable = false)
    private Member member;

}
