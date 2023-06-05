package com.adoptpet.server.adopt.dto.request;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class AdoptImageRequestDto {

    private Integer imgNo;
    private String imgUrl;
}
