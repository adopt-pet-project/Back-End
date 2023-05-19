package com.adoptpet.server.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MemberResponseDto {

    private Integer id;
    private String profile;
    private String name;
    private String location;
    private Activity activity;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Activity {
        private long document;
        private long comment;
        private long sanction;
    }
}
