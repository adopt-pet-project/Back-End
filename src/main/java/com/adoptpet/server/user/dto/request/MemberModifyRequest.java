package com.adoptpet.server.user.dto.request;

import lombok.Getter;

@Getter
public class MemberModifyRequest {

    private String name;
    private Image image;

    @Getter
    public static class Image {
        private String imageUrl;
        private Integer imageKey;
    }
}
