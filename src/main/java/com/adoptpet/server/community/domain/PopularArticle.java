package com.adoptpet.server.community.domain;


import com.adoptpet.server.commons.support.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "POPULAR_ARTICLE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PopularArticle extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "popular_no")
    private Integer popularNo;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private PopularEnum status;


    @OneToOne
    @JoinColumn(name = "article_no")
    private Community community;

    @Builder
    public PopularArticle(PopularEnum status, Community community) {
        this.status = status;
        this.community = community;
    }

    public static PopularArticle createHotArticle(Community community){
        return PopularArticle.builder()
                .status(PopularEnum.HOT)
                .community(community)
                .build();
    }

    public void updateStatusToWeekly(){
        this.status = PopularEnum.WEEKLY;
    }
}
