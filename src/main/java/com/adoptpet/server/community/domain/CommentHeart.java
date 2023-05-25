package com.adoptpet.server.community.domain;

import com.adoptpet.server.user.domain.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.*;

@Getter
@Entity
@Table(name = "COMMENT_HEART")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentHeart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "heart_no")
    private Integer heartNo;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Column(name = "reg_id")
    private String regId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_no")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "comment_no")
    private Comment comment;

    @Builder
    public CommentHeart(LocalDateTime regDate, String regId, Member member, Comment comment) {
        this.regDate = regDate;
        this.regId = regId;
        this.member = member;
        this.comment = comment;
    }

    public static CommentHeart createHeart(String regId, Member member, Comment comment) {
        return CommentHeart.builder()
                .regId(regId)
                .regDate(LocalDateTime.now())
                .member(member)
                .comment(comment)
                .build();
    }
}
