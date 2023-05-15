package com.adoptpet.server.adopt.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class AdoptBookmarkRequestDto {

    @NotNull
    private Integer saleNo;
}
