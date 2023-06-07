package com.adoptpet.server.commons.notification.domain;

import lombok.Getter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Getter
public enum NotifiTypeEnum {

    NOTICE("notice/","announcement"),
    ARTICLE_HOT("board/","documentHot"),
    ARTICLE_WEEK("board/","documentWeek"),
    CHAT("chat/","chat"),
    NOTE("myPage/noteLog/","note"),
    COMMENT("board/","comment"),
    REPLY("board/","recomment");

    private final String path;
    private final String alias;

    NotifiTypeEnum(String path,String alias) {
        this.path = path;
        this.alias = alias;
    }

}
