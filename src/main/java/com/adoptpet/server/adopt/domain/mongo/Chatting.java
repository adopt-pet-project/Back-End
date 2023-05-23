package com.adoptpet.server.adopt.domain.mongo;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Document(collection = "chatting")
@Getter @ToString
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// MongoDB Chatting 모델
public class Chatting {

    @Id
    private String id;
    private Integer chatRoomNo;
    private Integer senderNo;
    private String senderName;
    private String type;
    private String content;
    private LocalDateTime sendDate;

    @Builder
    public Chatting(Integer chatRoomNo, String type, String content, Integer senderNo, String senderName, LocalDateTime sendDate) {
        this.chatRoomNo = chatRoomNo;
        this.type = type;
        this.content = content;
        this.senderNo = senderNo;
        this.senderName = senderName;
        this.sendDate = sendDate;
    }

    public void setDate(LocalDateTime sendDate) {
        this.sendDate = sendDate;
    }

}
