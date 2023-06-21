package com.adoptpet.server.adopt.service;

import com.adoptpet.server.adopt.dto.aggregation.AggregationDto;
import com.adoptpet.server.adopt.repository.AdoptAggregationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdoptAggregationService {

    private final AdoptAggregationRepository aggregationRepository;


    @Transactional
    public void aggregation(AggregationDto aggregationDto) {
        switch (aggregationDto.getTarget()) {
            case ADOPT:
                aggregationBookmarkCount(aggregationDto.getSaleNo(), aggregationDto.getIsIncrease());
                break;
            case CHAT:
                aggregationChatCount(aggregationDto.getSaleNo(), aggregationDto.getIsIncrease());
                break;
        }
    }

    @Transactional
    public void aggregationChatCount(Integer saleNo, String isIncrease) {
        if (isIncrease.equals("true")) {
            aggregationRepository.increaseChatCount(saleNo);
            return;
        }

        aggregationRepository.decreaseChatCount(saleNo);
    }

    @Transactional
    public void aggregationBookmarkCount(Integer saleNo, String isIncrease) {
        if (isIncrease.equals("true")) {
            aggregationRepository.increaseBookmarkCount(saleNo);
            return;
        }

        aggregationRepository.decreaseBookmarkCount(saleNo);
    }
}
