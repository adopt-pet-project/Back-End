package com.adoptpet.server.adopt.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
public class ChattingDto {

    private Integer chatNo;

    private Integer createMember;

    private Integer joinMember;

    private Integer saleNo;

    private long regDate;
    private Participant participant;
    private LatestMessage latestMessage;

    private Long unReadCount;

    public void setUnReadCount(Long unReadCount) {
        this.unReadCount = unReadCount;
    }

    public ChattingDto(Integer chatNo, Integer createMember, Integer joinMember, Integer saleNo, LocalDateTime regDate, Participant participant) {
        this.chatNo = chatNo;
        this.createMember = createMember;
        this.joinMember = joinMember;
        this.saleNo = saleNo;
        this.regDate = regDate.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        this.participant = participant;
    }

    public void setLatestMessage(LatestMessage latestMessage) {
        this.latestMessage = latestMessage;
    }

    @Getter
    @AllArgsConstructor
    public static class Participant {
        private String username;
        private String profile;
    }

    @Getter
    public static class LatestMessage {
        private String context;
        private long sendAt;

        @Builder
        public LatestMessage(String context, LocalDateTime sendAt) {
            this.context = context;
            this.sendAt = sendAt.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        }
    }
}
