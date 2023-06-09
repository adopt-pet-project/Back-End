package com.adoptpet.server.community.dto.request;

import com.adoptpet.server.community.domain.BlindEnum;
import com.adoptpet.server.community.domain.LogicalDelEnum;
import com.adoptpet.server.community.dto.ArticleImageDto;
import com.adoptpet.server.community.dto.ArticleDto;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegisterArticleRequest {

    @Min(value = 0)
    @JsonProperty("categoryId")
    private Integer categoryNo;

    @NotBlank
    @Length(max = 100)
    @JsonProperty("title")
    private String title;

    @NotBlank
    @JsonProperty("context")
    private String content;

    @JsonProperty("imageList")
    private ArticleImageDto[] image;

    @JsonProperty("thumbnail")
    private String thumbnail;

    public ArticleDto toDto(String userId){
        ArticleDto.ArticleDtoBuilder builder = ArticleDto.builder()
                .categoryNo(this.categoryNo)
                .title(this.title)
                .content(this.content)
                .viewCount(0)
                .regId(userId)
                .modId(userId)
                .logicalDel(LogicalDelEnum.NORMAL)
                .blindYn(BlindEnum.NORMAL);
        // 등록한 이미지가 있을 경우
        if (this.image != null && this.image.length > 0) {
            builder.image(this.image)
                    .thumbnail(this.image[0].getImageUrl());
        // 등록한 이미지가 없을 경우
        } else {
            builder.image(null)
                    .thumbnail("NONE");
        }

        return builder.build();
    }
}
