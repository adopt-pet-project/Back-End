package com.adoptpet.server.community.dto.response;

import com.adoptpet.server.commons.image.dto.ImageInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ArticleInfoResponse {

    private Integer id; // articleNo
    private boolean mine;
    private Header header;
    private Context context;

    @Builder
    public ArticleInfoResponse(Integer articleNo, boolean mine, Header header, Context context) {
        this.id = articleNo;
        this.mine = mine;
        this.header = header;
        this.context = context;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Header{
        private String title;
        private Integer authorId; // memberNo
        private String username; //nickname
        private String profile;
        private Integer view;
        private Integer like;
        private Integer comment;
        private LocalDateTime publishedAt; //regDate
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Context{
        private String context; //content
        private List<ImageInfoDto> imageList; // images
    }
}
