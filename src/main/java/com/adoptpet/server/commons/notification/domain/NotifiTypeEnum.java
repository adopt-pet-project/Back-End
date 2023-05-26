package com.adoptpet.server.commons.notification.domain;

import lombok.Getter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Getter
public enum NotifiTypeEnum {

    NOTICE("notice/","announcement"),
    ARTICLE_HOT("article/","documentHot"),
    ARTICLE_WEEK("article/","documentWeek"),
    CHAT("chatRoom/","chat"),
    NOTE("note/","note"),
    COMMENT("article/","comment"),
    REPLY("article/","recomment");

    private final String path;
    private final String alias;

    NotifiTypeEnum(String path,String alias) {
        this.path = path;
        this.alias = alias;
    }

}
