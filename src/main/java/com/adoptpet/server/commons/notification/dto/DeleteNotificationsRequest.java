package com.adoptpet.server.commons.notification.dto;

import lombok.Getter;

@Getter
public class DeleteNotificationsRequest {
    private Long[] idList;
}
