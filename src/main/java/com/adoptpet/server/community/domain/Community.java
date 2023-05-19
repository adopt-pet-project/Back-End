package com.adoptpet.server.community.domain;


import com.adoptpet.server.commons.support.BaseTimeEntity;
import com.adoptpet.server.community.dto.CommunityDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "COMMUNITY")
@Getter
@Where(clause = "logical_del = '0'")
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

    @Column(name = "thumbnail")
    private String thumbnail;

    @OneToMany(mappedBy = "community")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "community")
    private List<ArticleHeart> articleHearts = new ArrayList<>();

    @OneToMany(mappedBy = "community")
    private List<ArticleBookmark> articleBookmarks = new ArrayList<>();

    @Builder
    public Community(Integer categoryNo, String title, String content, Integer viewCount, String regId, String modId, VisibleYnEnum visibleYn, LogicalDelEnum logicalDel, BlindYnEnum blindYn, String thumbnail) {
        this.categoryNo = categoryNo;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.regId = regId;
        this.modId = modId;
        this.visibleYn = visibleYn;
        this.logicalDel = logicalDel;
        this.blindYn = blindYn;
        this.thumbnail = thumbnail;
    }

    public void updateByArticleNo(CommunityDto communityDto, String modId) {
        this.categoryNo = communityDto.getCategoryNo();
        this.title = communityDto.getTitle();
        this.content = communityDto.getContent();
        this.modId = modId;
        this.visibleYn = communityDto.getVisibleYn();
    }

    public void deleteByLogicalDel(LogicalDelEnum logicalDel){
        this.logicalDel = logicalDel;
    }

    public void addThumbnail(String imageUrl) {
        this.thumbnail = imageUrl;
    }
}
