package com.adoptpet.server.community.domain;

import com.adoptpet.server.user.domain.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.*;

@Builder
@Entity
@Table(name = "ARTICLE_BOOKMARK")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleBookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_no")
    private Integer bookmarkNo;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Column(name = "reg_id")
    private String regId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "article_no")
    private Community community;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "member_no")
    private Member member;

    public static ArticleBookmark createArticleBookmark(String email, Member member, Community community){
        return ArticleBookmark.builder()
                .regId(email)
                .member(member)
                .community(community)
                .regDate(LocalDateTime.now())
                .build();
    }
}
