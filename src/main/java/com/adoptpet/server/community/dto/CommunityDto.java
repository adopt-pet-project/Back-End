package com.adoptpet.server.community.dto;

import com.adoptpet.server.community.domain.*;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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
    private VisibleYnEnum visibleYn;

    @NotBlank
    private LogicalDelEnum logicalDel;

    @NotBlank
    private BlindYnEnum blindYn;
}
