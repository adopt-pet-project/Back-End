package com.adoptpet.server.commons.notification.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class DeleteNotificationsRequest {
    private List<Long> idList;
}
