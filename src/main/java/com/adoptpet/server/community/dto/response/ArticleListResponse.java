package com.adoptpet.server.community.dto.response;

import com.adoptpet.server.community.dto.ArticleListDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import net.minidev.json.annotate.JsonIgnore;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ArticleListResponse {

    @JsonProperty
    private ArticleListDto hot;
    @JsonProperty
    private ArticleListDto weekly;
    @JsonProperty
    private List<ArticleListDto> list;
}
