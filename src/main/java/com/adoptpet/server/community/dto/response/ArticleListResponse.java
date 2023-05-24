package com.adoptpet.server.community.dto.response;

import com.adoptpet.server.community.dto.ArticleListDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import net.minidev.json.annotate.JsonIgnore;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleListResponse {
    private ArticleListDto hot;
    private ArticleListDto weekly;
    private List<ArticleListDto> list;
}
