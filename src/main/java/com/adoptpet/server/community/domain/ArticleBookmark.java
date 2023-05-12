package com.adoptpet.server.community.domain;

import com.adoptpet.server.user.domain.Member;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.*;

@Entity
@Table(name = "ARTICLE_BOOKMARK")
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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_no")
    private Member member;

    //== 연관관계 메서드 ==//
    public void addCommunity(Community community){
        this.community = community;
        community.getArticleBookmark(this);
    }

    private void addMember(Member member){
        this.member = member;
        //member.getArticleBookmark() 추가하기
    }


    //== 생성 메서드 ==//

    //== 조회 메서드 ==//

    //== 수정 메서드 ==//

    //== 비즈니스 로직 ==//


}
