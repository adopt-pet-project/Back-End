package com.adoptpet.server.community.dto.request;

import com.adoptpet.server.community.dto.ArticleImageDto;
import com.adoptpet.server.community.dto.ArticleDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateArticleRequest {
    @JsonProperty("categoryId")
    private Integer categoryNo;

    @JsonProperty("title")
    private String title;

    @JsonProperty("context")
    private String content;

    @JsonProperty("imageList")
    private ArticleImageDto[] image;

    public ArticleDto toDto(){
        ArticleDto.ArticleDtoBuilder builder = ArticleDto.builder()
                .categoryNo(this.categoryNo)
                .title(this.title)
                .content(this.content);
        // 등록한 이미지가 있을 경우
        if (this.image != null && this.image.length > 0) {
            builder.image(this.image)
                    .thumbnail(this.image[0].getImageUrl());
        // 등록한 이미지가 없을 경우
        } else {
            builder.image(null);
        }

        return builder.build();
    }
}
