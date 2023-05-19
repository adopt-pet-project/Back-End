package com.adoptpet.server.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ArticleInfoResponse {

    private Integer articleNo; //  community
    private String authorId; // community
    private Header header;
    private Context context;

    @Builder
    public ArticleInfoResponse(Integer articleNo, String authorId, Header header, Context context) {
        this.articleNo = articleNo;
        this.authorId = authorId;
        this.header = header;
        this.context = context;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Header{
        private String title; //  community
        private String author; //  user
        private Integer view; // community
        private Integer like; // articleHeart
        private Integer comment; // comment
        private LocalDateTime regDate; // community
        private String profile; // user
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Context{
        private String context; // community
        private List<String> image; // articleImage
    }
}
