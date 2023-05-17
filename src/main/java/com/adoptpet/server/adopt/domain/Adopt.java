package com.adoptpet.server.adopt.domain;

import com.adoptpet.server.adopt.dto.request.AdoptRequestDto;
import com.adoptpet.server.commons.security.dto.SecurityUserDto;
import com.adoptpet.server.commons.support.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ADOPT")
@DynamicInsert @Getter
@AllArgsConstructor @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Adopt extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_no")
    private Integer saleNo;

    @Column(name = "category_no", nullable = false)
    private Integer categoryNo;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "age")
    private String age;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "kind")
    private String kind;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "latitude")
    private Float latitude;

    @Column(name = "longitude")
    private Float longitude;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AdoptStatus status;

    @Column(name = "visible_yn")
    private String visibleYn;

    @Column(name = "logical_del")
    private Integer logicalDel;

    @Column(name = "blind_yn")
    private Integer blindYn;

    @Column(name = "reg_id")
    private String regId;

    @Column(name = "mod_id")
    private String modId;

    @OneToMany(mappedBy = "adopt", fetch = FetchType.LAZY)
    private List<AdoptBookmark> adoptBookmarks = new ArrayList<>();

    public void addRegIdAndModId(String regId, String modId) {
        addRegId(regId);
        this.modId = modId;
    }

    public void addThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void addRegId(String regId) {
        this.regId = regId;
    }

    // 분양 엔티티의 정보를 업데이트
    public void updateAdopt(AdoptRequestDto adoptDto, SecurityUserDto user) {
        this.categoryNo = adoptDto.getCategoryNo();
        this.title = adoptDto.getTitle();
        this.content = adoptDto.getContent();
        this.age = adoptDto.getAge();
        this.gender = adoptDto.getGender();
        this.latitude = adoptDto.getLatitude();
        this.longitude = adoptDto.getLongitude();
        this.modId = user.getEmail();
        this.address = adoptDto.getAddress();
    }
}
