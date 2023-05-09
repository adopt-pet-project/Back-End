package com.adoptpet.server.community.domain;

import lombok.Getter;

@Getter
public enum VisibleYnEnum {
    VISIBLE("Y"),
    UNVISIBLE("N");

    private final String value;

    VisibleYnEnum(String value){
        this.value = value;
    }
}
