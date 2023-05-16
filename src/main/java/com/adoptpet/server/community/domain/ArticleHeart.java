package com.adoptpet.server.community.domain;


import com.adoptpet.server.user.domain.Member;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.*;

@Entity
@Table(name = "ARTICLE_HEART")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleHeart {
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
    @JoinColumn(name = "article_no")
    private Community community;

}
