package com.adoptpet.server.adopt.dto.response;

import com.adoptpet.server.adopt.domain.AdoptStatus;
import com.adoptpet.server.adopt.domain.Gender;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdoptResponseDto {

    private Integer id;
    private String authorId;
    private String[] images;
    private Header header;
    private Metadata metadata;
    private Context context;
    private Author author;

    public AdoptResponseDto(Integer id, String authorId, Header header, Metadata metadata, Context context, Author author) {
        this.id = id;
        this.authorId = authorId;
        this.header = header;
        this.metadata = metadata;
        this.context = context;
        this.author = author;
    }

    public void addImages(String[] images) {
        this.images = images;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    public static class Header {
        private String title;
        private AdoptStatus status;
        private LocalDateTime regDate;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Metadata {
        private Gender gender;
        private String age;
        private String name;
        private String species;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Context {
        private String context;
        private long bookmark;
        private long chat;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Author {
        private String author;
        private String profile;
        private String location;
    }


}
