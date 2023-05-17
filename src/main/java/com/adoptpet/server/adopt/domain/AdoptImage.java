package com.adoptpet.server.adopt.domain;

import com.adoptpet.server.commons.support.BaseImageEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ADOPT_IMAGE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdoptImage extends BaseImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "picture_no")
    private Integer pictureNo;

    @Column(name = "sale_no")
    private Integer saleNo;

    @Column(name = "sort")
    private Integer sort;

    // Test 코드용 생성자
    public AdoptImage(String url, String name, String type, Integer saleNo) {
        addImageName(name);
        addImageUrl(url);
        addImageType(type);
        this.saleNo = saleNo;
    }

}
