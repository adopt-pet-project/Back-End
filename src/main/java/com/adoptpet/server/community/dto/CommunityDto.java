package com.adoptpet.server.community.dto;

import com.adoptpet.server.community.domain.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommunityDto {

    private Integer articleNo;

    private String title;

    private String content;

    private Integer viewCount;

    private String regId;

    private String modId;

    private VisibleYnEnum visibleYn;

    private LogicalDelEnum logicalDel;

    private BlindYnEnum blindYn;
}
