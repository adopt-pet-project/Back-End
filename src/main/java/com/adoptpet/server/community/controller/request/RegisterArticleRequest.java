package com.adoptpet.server.community.controller.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegisterArticleRequest {

    private String title;
    private String content;
    private String regId;
    private String visibleYn;
}
