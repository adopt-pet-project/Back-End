package com.adoptpet.server.adopt.dto.chat;


import com.adoptpet.server.adopt.domain.mongo.Chatting;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
public class Message implements Serializable {

    @NotNull
    private Integer chatNo;

    @NotNull
    private String contentType;

    @NotNull
    private String content;

    private String senderName;

    private Integer senderNo;

    private LocalDateTime sendTime;

    public void setSendTimeAndSender(LocalDateTime sendTime, Integer senderNo, String senderName) {
        this.senderName = senderName;
        this.sendTime = sendTime;
        this.senderNo = senderNo;
    }

    public Chatting convertEntity() {
        return Chatting.builder()
                .senderName(senderName)
                .senderNo(senderNo)
                .chatRoomNo(chatNo)
                .content(contentType)
                .content(content)
                .sendDate(sendTime)
                .build();
    }
}
