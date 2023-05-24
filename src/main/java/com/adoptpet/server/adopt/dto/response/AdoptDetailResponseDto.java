package com.adoptpet.server.adopt.dto.response;

import com.adoptpet.server.adopt.domain.AdoptStatus;
import com.adoptpet.server.adopt.domain.Gender;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdoptDetailResponseDto {

    private Integer id;
    private List<AdoptImageResponseDto> imageList;
    private boolean isMine;
    private Coords coords;
    private Header header;
    private Metadata metadata;
    private Context context;
    private Author author;

    public AdoptDetailResponseDto(Integer id, Header header, Metadata metadata, Context context, Author author, Coords coords) {
        this.id = id;
        this.header = header;
        this.metadata = metadata;
        this.context = context;
        this.author = author;
        this.coords = coords;
    }

    public void addIsMine(boolean isMine) {
        this.isMine = isMine;
    }
    public void addImages(List<AdoptImageResponseDto> imageList) {
        this.imageList = imageList;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Coords {
        private Float latitude;
        private Float longitude;
        private String address;
    }


    @Getter
    @Setter
    public static class Header {
        private String title;
        private Integer status;
        private long publishedAt;

        public Header(String title, Integer status, LocalDateTime publishedAt) {
            this.title = title;
            this.status = status;
            this.publishedAt = publishedAt.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        }
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
        private Integer id;
        private String username;
        private String profile;
        private String address;
    }


}
