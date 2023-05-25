package com.adoptpet.server.community.domain;


import com.adoptpet.server.user.domain.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.*;
@Getter
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

    @Column(name = "reg_id")
    private String regId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_no")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "article_no")
    private Community community;

    @Builder
    public ArticleHeart(LocalDateTime regDate, String regId, Member member, Community community) {
        this.regDate = regDate;
        this.regId = regId;
        this.member = member;
        this.community = community;
    }

    public static ArticleHeart createArticleHeart(String regId, Community community, Member member){
        return ArticleHeart.builder()
                .regId(regId)
                .regDate(LocalDateTime.now())
                .community(community)
                .member(member)
                .build();
    }
}
