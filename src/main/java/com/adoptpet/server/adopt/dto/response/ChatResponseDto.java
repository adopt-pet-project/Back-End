package com.adoptpet.server.adopt.dto.response;

import com.adoptpet.server.adopt.domain.mongo.Chatting;
import lombok.Getter;

import java.time.ZoneId;

@Getter
public class ChatResponseDto {
    private String id;
    private Integer chatRoomNo;
    private Integer senderNo;
    private String senderName;
    private String type;
    private String content;
    private long sendDate;
    private long readCount;

    public ChatResponseDto(Chatting chatting) {
        this.id = chatting.getId();
        this.chatRoomNo = chatting.getChatRoomNo();
        this.senderNo = chatting.getSenderNo();
        this.senderName = chatting.getSenderName();
        this.type = chatting.getType();
        this.content = chatting.getContent();
        this.sendDate = chatting.getSendDate().atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        this.readCount = chatting.getReadCount();
    }
}
