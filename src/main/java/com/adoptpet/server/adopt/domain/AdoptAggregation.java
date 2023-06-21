package com.adoptpet.server.adopt.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ADOPT_AGGREGATION")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdoptAggregation {

    @Id
    @Column(name = "sale_no")
    private Integer saleNo;

    @Column(name = "chat_count")
    private Integer chatCount;

    @Column(name = "bookmark_count")
    private Integer bookmarkCount;

    @Builder
    public AdoptAggregation(Integer saleNo, Integer chatCount, Integer bookmarkCount) {
        this.saleNo = saleNo;
        this.bookmarkCount = bookmarkCount;
        this.chatCount = chatCount;
    }
}
