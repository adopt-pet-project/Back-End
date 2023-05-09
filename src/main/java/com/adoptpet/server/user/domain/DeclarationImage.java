package com.adoptpet.server.user.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "DECLARATION_IMAGE")
public class DeclarationImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "picture_no")
    private Integer pictureNo;

    @Column(name = "decl_no", nullable = false)
    private Integer declNo;

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
