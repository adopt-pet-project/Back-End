package com.adoptpet.server.user.domain;

import com.adoptpet.server.commons.support.BaseImageEntity;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "PROFILE_IMAGE")
public class ProfileImage extends BaseImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "picture_no")
    private Integer pictureNo;

    @Column(name = "member_no", unique = true)
    private Integer memberNo;

}