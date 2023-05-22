package com.adoptpet.server.community.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleImageDto {
    @JsonProperty("id")
    private Integer imageNo;
    @JsonProperty("url")
    private String imageUrl;
}
