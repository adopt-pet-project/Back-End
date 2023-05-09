package com.adoptpet.server.user.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "NOTIFICATION")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "noti_no")
    private Integer notiNo;

    @Column(name = "member_no", nullable = false)
    private Integer memberNo;

    @Column(name = "division", nullable = false, length = 20)
    private String division;

    @Column(name = "content", nullable = false, length = 100)
    private String content;

    @Column(name = "corres_url", nullable = false, columnDefinition = "text")
    private String corresUrl;

    @Column(name = "read_status", nullable = false, length = 10, columnDefinition = "varchar(10) default 'N'")
    private String readStatus;

    @Column(name = "reg_date", nullable = false)
    private LocalDateTime regDate;

    @Column(name = "mod_date", nullable = false)
    private LocalDateTime modDate;
}
