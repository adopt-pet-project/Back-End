package com.adoptpet.server.adopt.dto.request;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@Builder
public class AdoptImageRequestDto {

    private Integer imgNo;
    private String imgUrl;
}
