package com.adoptpet.server.adopt.dto.response;


import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@ToString
public class ChatRoomResponseDto {

    private Integer chatNo;

    private Integer createMember;

    private Integer joinMember;

    private Integer saleNo;

    private long regDate;

    private Long unReadCount;

    public void setUnReadCount(Long unReadCount) {
        this.unReadCount = unReadCount;
    }

    public ChatRoomResponseDto(Integer chatNo, Integer createMember, Integer joinMember, Integer saleNo, LocalDateTime regDate) {
        this.chatNo = chatNo;
        this.createMember = createMember;
        this.joinMember = joinMember;
        this.saleNo = saleNo;
        this.regDate = regDate.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
    }
}
