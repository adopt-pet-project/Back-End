package com.adoptpet.server.community.domain;

import com.adoptpet.server.commons.domain.BaseImageEntity;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Table(name = "COMMUNITY_IMAGE")
public class CommunityImage extends BaseImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "picture_no")
    private Integer pictureNo;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "article_no")
    private Community community;

    //== 연관관계 메서드 ==//
    public void addCommunity(Community community){
        this.community = community;
        community.getArticleImage(this);
    }

    //== 생성 메서드 ==//

    //== 조회 메서드 ==//

    //== 수정 메서드 ==//

    //== 비즈니스 로직 ==//

}
