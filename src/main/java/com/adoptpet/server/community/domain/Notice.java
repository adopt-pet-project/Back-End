package com.adoptpet.server.community.domain;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "NOTICE")
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_no")
    private Integer noticeNo;

    @Column(name = "category_no", nullable = false)
    private Integer categoryNo;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "text")
    private String content;

    @Column(name = "reg_date", nullable = false)
    private LocalDateTime regDate;

    @Column(name = "reg_id", nullable = false, length = 50)
    private String regId;

    @Column(name = "mod_date", nullable = false)
    private LocalDateTime modDate;

    @Column(name = "mod_id", nullable = false, length = 50)
    private String modId;
}
