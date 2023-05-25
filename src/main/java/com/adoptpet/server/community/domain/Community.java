package com.adoptpet.server.community.domain;


import com.adoptpet.server.commons.support.BaseTimeEntity;
import com.adoptpet.server.community.dto.ArticleDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "logical_del")
    private LogicalDelEnum logicalDel;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "blind_yn")
    private BlindEnum blindYn;

    @Column(name = "thumbnail")
    private String thumbnail;

    @OneToMany(mappedBy = "community")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "community")
    private List<ArticleHeart> articleHearts = new ArrayList<>();

    @OneToMany(mappedBy = "community")
    private List<ArticleBookmark> articleBookmarks = new ArrayList<>();

    @Builder
    public Community(Integer categoryNo, String title, String content, Integer viewCount, String regId, String modId, LogicalDelEnum logicalDel, BlindEnum blindYn, String thumbnail) {
        this.categoryNo = categoryNo;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.regId = regId;
        this.modId = modId;
        this.logicalDel = logicalDel;
        this.blindYn = blindYn;
        this.thumbnail = thumbnail;
    }

    public void updateArticleByMod(ArticleDto articleDto, String modId) {
        this.categoryNo = articleDto.getCategoryNo();
        this.title = articleDto.getTitle();
        this.content = articleDto.getContent();
        this.modId = modId;
        // 이미지 배열 중 가장 첫번째 URL을 썸네일 이미지로 넣어준다.
        if (Objects.nonNull(articleDto.getImage())) {
            this.thumbnail = articleDto.getImage()[0].getImageUrl();
        } else {
            this.thumbnail = "NONE";
        }
    }

    public void deleteByLogicalDel(LogicalDelEnum logicalDel){
        this.logicalDel = logicalDel;
    }

    public Integer getHeartCnt(){
        return this.getArticleHearts().size();
    }
}
