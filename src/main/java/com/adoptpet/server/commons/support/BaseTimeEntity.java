package com.adoptpet.server.commons.support;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class) //entity 이벤트 발생시 처리. 시간에 대한 값을 자동으로 입력
public abstract class BaseTimeEntity {

    @CreatedDate//생성일시 자동입력
    @Column(name = "leg_date")
    private LocalDateTime legDate;

    @LastModifiedDate
    @Column(name = "mod_date")
    private LocalDateTime modDate;
}