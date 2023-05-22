package com.adoptpet.server.adopt.dto.chat;


import com.adoptpet.server.adopt.domain.mongo.Chatting;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {

    private Integer chatNo;
    private String contentType;
    private String content;

    private LocalDateTime sendTime;

    public void setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime;
    }

    public Chatting convertEntity() {
        return Chatting.builder()
                .chatRoomNo(chatNo)
                .content(contentType)
                .content(content)
                .build();
    }
}
