package com.adoptpet.server.adopt.domain;

import com.adoptpet.server.user.domain.Member;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ADOPT_BOOKMARK")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdoptBookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_no")
    private Integer bookmarkNo;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Column(name = "reg_id")
    private String regId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_no")
    private Adopt adopt;

    public void addMemberAndAdopt(Member member, Adopt adopt) {
        this.member = member;
        this.adopt = adopt;
    }

    public AdoptBookmark(String regId, Adopt adopt, Member member) {
        this.regId = regId;
        addMemberAndAdopt(member, adopt);
        this.regDate = LocalDateTime.now();
    }
}
