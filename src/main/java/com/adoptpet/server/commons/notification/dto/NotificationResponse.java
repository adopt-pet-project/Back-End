package com.adoptpet.server.commons.notification.dto;

import com.adoptpet.server.commons.notification.domain.NotifiTypeEnum;
import com.adoptpet.server.commons.notification.domain.Notification;
import com.adoptpet.server.commons.util.LocalDateTimeToArray;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NotificationResponse {

    private Long id;
    private String type;
    private String content;
    private String url;
    private Integer[] publishedAt;
    private boolean read;
    private String name;

    @Builder
    public NotificationResponse(Long id, NotifiTypeEnum type , String content, String url, LocalDateTime publishedAt, boolean read, String name) {
        this.id = id;
        this.type = type.getAlias();
        this.content = content;
        this.url = url;
        this.publishedAt = LocalDateTimeToArray.convert(publishedAt);
        this.read = read;
        this.name = name;
    }

    public static NotificationResponse from(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getNotiNo())
                .type(notification.getType())
                .content(notification.getContent())
                .url(notification.getUrl())
                .publishedAt(notification.getRegDate())
                .read(notification.isRead())
                .name(notification.getMember().getNickname())
                .build();
    }
}