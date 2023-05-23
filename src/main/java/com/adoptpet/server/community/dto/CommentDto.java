package com.adoptpet.server.community.dto;

import com.adoptpet.server.community.domain.BlindYnEnum;
import com.adoptpet.server.community.domain.LogicalDelEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommentDto {
    private String content;
    private Integer parentNo;
    private String regId;
    private String modId;
    private LogicalDelEnum logicalDel;
    private BlindYnEnum blindYnEnum;
}
