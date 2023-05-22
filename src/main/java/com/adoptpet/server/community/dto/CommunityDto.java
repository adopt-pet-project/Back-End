package com.adoptpet.server.community.dto;

import com.adoptpet.server.community.domain.*;
import lombok.*;
import javax.validation.constraints.NotBlank;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommunityDto {

    @NotBlank
    private Integer categoryNo;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    private Integer viewCount;

    @NotBlank
    private String regId;

    @NotBlank
    private String modId;

    @NotBlank
    private LogicalDelEnum logicalDel;

    @NotBlank
    private BlindYnEnum blindYn;

    private String thumbnail;

    private ArticleImageDto[] image;

    public void addImgNo(ArticleImageDto[] image) {
        this.image = image;
    }
}
