package com.adoptpet.server.community.domain;

import com.adoptpet.server.commons.support.BaseImageEntity;
import lombok.*;

import javax.persistence.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "COMMUNITY_IMAGE")
@AllArgsConstructor
@Builder
public class CommunityImage extends BaseImageEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "picture_no")
    private Integer pictureNo;

    @Column(name = "article_no")
    private Integer articleNo;

    @Column(name = "sort")
    private Integer sort;
}
