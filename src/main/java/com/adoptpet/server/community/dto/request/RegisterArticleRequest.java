package com.adoptpet.server.community.dto.request;

import com.adoptpet.server.community.domain.VisibleYnEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegisterArticleRequest {

    @NotNull
    @JsonProperty("categoryNo")
    private Integer categoryNo;

    @JsonProperty("title")
    @NotBlank
    @Length(max = 100)
    private String title;

    @JsonProperty("context")
    @NotBlank
    private String content;

    @JsonProperty("visible")
    private VisibleYnEnum visibleYn;
}
