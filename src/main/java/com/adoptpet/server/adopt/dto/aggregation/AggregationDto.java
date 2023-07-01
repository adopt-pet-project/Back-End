package com.adoptpet.server.adopt.dto.aggregation;

import lombok.*;

import java.io.Serializable;
@Getter @ToString @Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class AggregationDto implements Serializable {

    private Integer saleNo;
    private String isIncrease;
    private AggregationTarget target;
}
