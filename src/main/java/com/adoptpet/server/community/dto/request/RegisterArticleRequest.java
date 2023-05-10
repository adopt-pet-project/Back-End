package com.adoptpet.server.community.dto.request;

import com.adoptpet.server.community.domain.Community;
import com.adoptpet.server.community.domain.VisibleYnEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegisterArticleRequest {

    private String title;
    private String content;
    private String regId;
    private String visibleYn;

    public Community toEntity() {

        //String -> Enum
        VisibleYnEnum visible = VisibleYnEnum.from(this.visibleYn);

        return Community.builder()
                .title(this.title)
                .content(this.content)
                .regId(this.regId)
                .modId(this.regId)
                .visibleYn(visible)
                .build();
    }
}
