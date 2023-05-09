package com.adoptpet.server.community.domain;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ARTICLE_HEART")
public class ArticleHeart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "heart_no")
    private Integer heartNo;

    @Column(name = "member_no", nullable = false)
    private Integer memberNo;

    @Column(name = "article_no", nullable = false)
    private Integer articleNo;

    @Column(name = "reg_date", nullable = false)
    private LocalDateTime regDate;

    @Column(name = "mod_date", nullable = false)
    private LocalDateTime modDate;

    @Column(name = "reg_id", nullable = false, length = 50)
    private String regId;

    @Column(name = "mod_id", nullable = false, length = 50)
    private String modId;

}
