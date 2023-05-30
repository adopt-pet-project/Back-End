package com.adoptpet.server.adopt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


@AllArgsConstructor
@Builder
@Getter
public class ChattingHistoryResponseDto {
    private String email;
    private List<ChatResponseDto> chatList;
}
