package com.adoptpet.server.community.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ARTICLE_BOOKMARK")
public class ArticleBookMark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_no")
    private Integer bookmarkNo;

    @Column(name = "member_no", nullable = false)
    private Integer memberNo;

    @Column(name = "article_no", nullable = false)
    private Integer articleNo;

    @Column(name = "reg_date", nullable = false)
    private LocalDateTime regDate;

    @Column(name = "reg_id", nullable = false, length = 50)
    private String regId;

}
