package com.adoptpet.server.community.dto;

import com.adoptpet.server.community.domain.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommunityDto {

    private Integer categoryNo;

    private String title;

    private String content;

    private Integer viewCount;

    private String regId;

    private String modId;

    private VisibleYnEnum visibleYn;

    private LogicalDelEnum logicalDel;

    private BlindYnEnum blindYn;

    private List<CommunityImage> communityImages = new ArrayList<>();


}
