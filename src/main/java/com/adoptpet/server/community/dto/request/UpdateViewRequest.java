package com.adoptpet.server.community.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.Min;

@Getter
public class UpdateViewRequest {
    @Min(value = 0)
    @JsonProperty("id")
    private Integer articleNo;
}
