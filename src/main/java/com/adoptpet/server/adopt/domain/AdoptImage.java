package com.adoptpet.server.adopt.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ADOPT_IMAGE")
public class AdoptImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "picture_no")
    private Integer pictureNo;

    @Column(name = "sale_no", nullable = false)
    private Integer saleNo;

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
