package com.adoptpet.server.community.dto;

import com.adoptpet.server.commons.util.LocalDateTimeUtils;
import com.adoptpet.server.community.domain.LogicalDelEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NoteDto {
    @JsonProperty("id")
    private Integer noteNo; // note
    @JsonProperty("targetId")
    private Integer opponentNo;
    @JsonProperty("name")
    private String nickName; // member
    @JsonProperty("contents")
    private String content; // noteHistory
    @JsonProperty("mine")
    private boolean mine;
    @JsonProperty("publishedAt")
    private String regDate; // noteHistory
    @JsonProperty("checked")
    private boolean readStatus; // note
    @JsonProperty("deleteStatus")
    private LogicalDelEnum logicalDel;

    @Builder
    public NoteDto(Integer noteNo,Integer opponentNo ,String nickName, String content, boolean mine, LocalDateTime regDate, boolean readStatus,LogicalDelEnum logicalDel) {
        this.opponentNo = opponentNo;
        this.noteNo = noteNo;
        this.nickName = nickName;
        this.content = content;
        this.mine = mine;
        this.regDate = LocalDateTimeUtils.toString(regDate);
        this.readStatus = readStatus;
        this.logicalDel = logicalDel;
    }
}
