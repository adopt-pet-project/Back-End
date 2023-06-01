package com.adoptpet.server.community.dto;

import com.adoptpet.server.commons.util.LocalDateTimeUtils;
import com.adoptpet.server.community.domain.LogicalDelEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NoteHistoryDto {

    @JsonProperty("id")
    private Integer historyNo;
    @JsonProperty("mine")
    private boolean mine;
    @JsonProperty("contents")
    private String content;
    @JsonProperty("publishedAt")
    private String regDate;

    @JsonProperty("deleteStatus")
    private LogicalDelEnum logicalDel;

    @Builder
    public NoteHistoryDto(Integer historyNo, boolean mine, String content, LocalDateTime regDate, LogicalDelEnum logicalDel) {
        this.historyNo = historyNo;
        this.mine = mine;
        this.content = content;
        this.regDate = LocalDateTimeUtils.toString(regDate);
        this.logicalDel = logicalDel;
    }
}
