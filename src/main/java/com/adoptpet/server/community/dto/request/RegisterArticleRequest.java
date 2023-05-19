package com.adoptpet.server.community.dto.request;

import com.adoptpet.server.community.domain.BlindYnEnum;
import com.adoptpet.server.community.domain.LogicalDelEnum;
import com.adoptpet.server.community.domain.VisibleYnEnum;
import com.adoptpet.server.community.dto.ArticleImageDto;
import com.adoptpet.server.community.dto.CommunityDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegisterArticleRequest {

    @NotNull
    @JsonProperty("categoryNo")
    private Integer categoryNo;

    @JsonProperty("title")
    @NotBlank
    @Length(max = 100)
    private String title;

    @JsonProperty("content")
    @NotBlank
    private String content;

    @JsonProperty("visible")
    private VisibleYnEnum visibleYn;

    @JsonProperty("image")
    private ArticleImageDto[] image;

    @JsonProperty("thumbnail")
    private String thumbnail;

    public CommunityDto toDto(String userId){
        CommunityDto.CommunityDtoBuilder builder = CommunityDto.builder()
                .categoryNo(this.categoryNo)
                .title(this.title)
                .content(this.content)
                .viewCount(0)
                .regId(userId)
                .modId(userId)
                .visibleYn(this.visibleYn)
                .logicalDel(LogicalDelEnum.NORMAL)
                .blindYn(BlindYnEnum.NORMAL);
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
