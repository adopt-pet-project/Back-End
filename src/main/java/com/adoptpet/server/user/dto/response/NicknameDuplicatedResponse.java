package com.adoptpet.server.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NicknameDuplicatedResponse {

    private boolean isDuplicated;

    public static NicknameDuplicatedResponse create(boolean isDuplicated) {
        return new NicknameDuplicatedResponse(isDuplicated);
    }
}


