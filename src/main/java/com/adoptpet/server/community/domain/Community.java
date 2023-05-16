package com.adoptpet.server.community.domain;


import com.adoptpet.server.commons.support.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "COMMUNITY")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Community extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_no")
    private Integer articleNo;

    @Column(name = "category_no",nullable = false)
    private Integer categoryNo;

    @Column(name = "title",nullable = false, length = 100)
    private String title;

    @Column(name = "content",nullable = false)
    private String content;

    @Column(name = "view_cnt")
    private Integer viewCount;

    @Column(name = "reg_id")
    private String regId;

    @Column(name = "mod_id")
    private String modId;

    @Column(name = "visible_yn")
    @Convert(converter = VisibleYnEnum.VisibleConverter.class)
    private VisibleYnEnum visibleYn;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "logical_del")
    private LogicalDelEnum logicalDel;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "blind_yn")
    private BlindYnEnum blindYn;

    @OneToMany(mappedBy = "community")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "community")
    private List<ArticleHeart> articleHearts = new ArrayList<>();

    @OneToMany(mappedBy = "community")
    private List<ArticleBookmark> articleBookmarks = new ArrayList<>();

    @Builder
    public Community(String title, String content, Integer viewCount,
                     String regId, String modId, VisibleYnEnum visibleYn,
                     LogicalDelEnum logicalDel, BlindYnEnum blindYn) {
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.regId = regId;
        this.modId = modId;
        this.visibleYn = visibleYn;
        this.logicalDel = logicalDel;
        this.blindYn = blindYn;
    }

    //== 연관관계 메서드 ==//
    public void getComment(Comment comment){
        this.comments.add(comment);
    }

    public void getArticleHeart(ArticleHeart articleHeart) {
        this.articleHearts.add(articleHeart);
    }

    public void getArticleBookmark(ArticleBookmark articleBookMark) {
        this.articleBookmarks.add(articleBookMark);
    }


    //== 생성 메서드 ==//

    //== 조회 메서드 ==//

    //== 수정 메서드 ==//

    //== 비즈니스 로직 ==//

}
