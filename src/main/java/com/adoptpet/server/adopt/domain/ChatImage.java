package com.adoptpet.server.adopt.domain;

import com.adoptpet.server.commons.support.BaseImageEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "CHAT_IMAGE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatImage extends BaseImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "picture_no")
    private Integer pictureNo;

    @Column(name = "chat_no")
    private Integer chatNo;

    @Column(name = "sort")
    private Integer sort;

}
