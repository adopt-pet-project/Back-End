package com.adoptpet.server.adopt.domain;

import com.adoptpet.server.commons.support.BaseImageEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ADOPT_IMAGE")
public class AdoptImage extends BaseImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "picture_no")
    private Integer pictureNo;

    @Column(name = "sale_no", nullable = false)
    private Integer saleNo;

}
