package com.adoptpet.server.community.dto.response;


import lombok.Getter;

@Getter
public class HeartCountResponse {
    private Integer like;

    public HeartCountResponse(Integer like) {
        this.like = like;
    }
}
