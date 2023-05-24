package com.adoptpet.server.adopt.domain.mongo;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;


@Document(collection = "chatting")
@Getter @ToString
@Setter
@AllArgsConstructor @Builder
@NoArgsConstructor
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
    private long readCount;

}
