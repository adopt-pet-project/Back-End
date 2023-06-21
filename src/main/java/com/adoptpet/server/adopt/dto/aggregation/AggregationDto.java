package com.adoptpet.server.adopt.dto.aggregation;

import lombok.*;

import java.io.Serializable;
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AggregationDto implements Serializable {

    private Integer saleNo;
    private String isIncrease;
    private AggregationTarget target;
}
