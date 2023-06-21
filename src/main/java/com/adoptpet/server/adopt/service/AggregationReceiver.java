package com.adoptpet.server.adopt.service;

import com.adoptpet.server.adopt.dto.aggregation.AggregationDto;
import com.adoptpet.server.commons.util.ConstantUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AggregationReceiver {

    private final AdoptAggregationService aggregationService;

    @KafkaListener(topics = ConstantUtil.KAFKA_AGGREGATION, containerFactory = "kafkaAggregationContainerFactory")
    public void aggregation(AggregationDto aggregationDto) {
        log.info("요청 도착 = {}", aggregationDto);
        aggregationService.aggregation(aggregationDto);
    }
}
