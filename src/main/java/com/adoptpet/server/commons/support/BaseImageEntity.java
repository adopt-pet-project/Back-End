package com.adoptpet.server.commons.support;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseImageEntity{

    @CreatedDate //생성일자 자동입력
    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Column(name = "reg_id")
    private String regId;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "image_type")
    private String imageType;

    public void addImageUrl(String url){
        this.imageUrl = url;
    }

    public void addRagId(String regId){
        this.regId = regId;
    }
    public void addImageName(String name){
        this.imageName = name;
    }

    public void addImageType(String type){
        this.imageType = type;
    }
}
