package com.adoptpet.server.community.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "COMMUNITY_IMAGE")
public class CommunityImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "picture_no")
    private Integer pictureNo;

    @Column(name = "article_no", nullable = false)
    private Integer articleNo;

    @Column(name = "reg_date", nullable = false)
    private LocalDateTime regDate;

    @Column(name = "reg_id", nullable = false, length = 50)
    private String regId;

    @Column(name = "image_url", nullable = false, length = 255)
    private String imageUrl;

    @Column(name = "image_name", nullable = false, length = 255)
    private String imageName;

    @Column(name = "image_type", nullable = false, length = 20)
    private String imageType;
}
