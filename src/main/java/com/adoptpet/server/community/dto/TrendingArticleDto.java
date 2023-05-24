package com.adoptpet.server.community.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TrendingArticleDto {
    private Integer articleNo;
    private Integer likeCnt;
}
