package com.adoptpet.server.user.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "FEEDBACK")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_no")
    private Integer feedNo;

    @Column(name = "reg_date", nullable = false)
    private LocalDateTime regDate;

    @Column(name = "mod_date", nullable = false)
    private LocalDateTime modDate;

    @Column(name = "content", nullable = false, columnDefinition = "text")
    private String content;

    @Column(name = "reg_id", nullable = false, length = 50)
    private String regId;

    @Column(name = "ans_date")
    private LocalDateTime ansDate;

    @Column(name = "ans_content", columnDefinition = "text")
    private String ansContent;

    @Column(name = "ans_id", nullable = false, length = 50, columnDefinition = "varchar(50) default 'None'")
    private String ansId;

    @Column(name = "status", nullable = false, length = 10, columnDefinition = "varchar(10) default 'N'")
    private String status;

    @Column(name = "visible_yn", nullable = false, length = 10, columnDefinition = "varchar(10) default 'Y'")
    private String visibleYn;

}
