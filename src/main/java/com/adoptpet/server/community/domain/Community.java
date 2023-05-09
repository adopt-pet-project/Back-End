package com.adoptpet.server.community.domain;


import com.adoptpet.server.user.domain.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "COMMUNITY")
public class Community {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_no")
    private Integer articleNo;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "view_cnt", nullable = false)
    private Integer viewCount = 0;

    @Column(name = "reg_date", nullable = false)
    private LocalDateTime regDate;

    @Column(name = "mod_date", nullable = false)
    private LocalDateTime modDate;

    @Column(name = "reg_id", nullable = false)
    private String regId;

    @Column(name = "mod_id", nullable = false)
    private String modId;

    @Column(name = "visible_yn", nullable = false)
    private String visibleYn;

    @Column(name = "logical_del")
    private Integer logicalDel;

    @Column(name = "blind_yn")
    private Integer blindYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_no", nullable = false)
    private Category category;
}
