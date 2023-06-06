package com.adoptpet.server.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberModifyRequest {

    private String name;
    private Image image;

    @Getter
    @AllArgsConstructor
    public static class Image {
        private String imageUrl;
        private Integer imageKey;
    }
}
