package com.adoptpet.server.adopt.dto.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "chatRoom")
public class ChatRoom {

    @Id
    private String id;

    @Indexed
    private Integer chatroomNo;

    private String email;

    @Builder
    public ChatRoom(Integer chatroomNo, String email) {
        this.chatroomNo = chatroomNo;
        this.email = email;
    }
}
