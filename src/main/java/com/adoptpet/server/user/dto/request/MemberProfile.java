package com.adoptpet.server.user.dto.request;

import com.adoptpet.server.user.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Builder
public class MemberProfile {

    private String nickname;
    private String email;
    private String provider;

    public Member toMember() {
        return Member.builder()
                .nickname(nickname)
                .email(email)
                .platform(provider)
                .build();
    }
}
