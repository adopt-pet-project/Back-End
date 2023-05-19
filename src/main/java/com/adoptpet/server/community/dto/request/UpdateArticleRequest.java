package com.adoptpet.server.community.dto.request;

import com.adoptpet.server.community.domain.VisibleYnEnum;
import com.adoptpet.server.community.dto.ArticleImageDto;
import com.adoptpet.server.community.dto.CommunityDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateArticleRequest {
    @JsonProperty("categoryNo")
    private final Integer categoryNo;
    @JsonProperty("title")
    private final String title;
    @JsonProperty("content")
    private final String content;
    @JsonProperty("visible")
    private VisibleYnEnum visibleYn;
    @JsonProperty("imageNo")
    private ArticleImageDto[] articleImageDto;

    public CommunityDto toDto(){
        return CommunityDto.builder()
                .categoryNo(this.categoryNo)
                .title(this.title)
                .visibleYn(this.visibleYn)
                .image(this.articleImageDto)
                .thumbnail(this.articleImageDto[0].getImageUrl())
                .build();
    }
}
