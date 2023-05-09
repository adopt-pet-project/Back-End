package com.adoptpet.server.adopt.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ADOPT_BOOKMARK")
public class AdoptBookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_no")
    private Integer bookmarkNo;

    @Column(name = "sale_no", nullable = false)
    private Integer saleNo;

    @Column(name = "member_no", nullable = false)
    private Integer memberNo;

    @Column(name = "reg_date", nullable = false)
    private LocalDateTime regDate;

    @Column(name = "reg_id", nullable = false, length = 50)
    private String regId;
}
