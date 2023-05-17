package com.adoptpet.server.adopt.domain;

import com.adoptpet.server.commons.support.BaseImageEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "ADOPT_IMAGE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdoptImage extends BaseImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "picture_no")
    private Integer pictureNo;

    @Column(name = "sale_no", nullable = false)
    private Integer saleNo;

    @Builder
    public AdoptImage(Integer pictureNo, Integer saleNo) {
        this.pictureNo = pictureNo;
        this.saleNo = saleNo;
    }
}
