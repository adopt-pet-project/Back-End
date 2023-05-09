package com.adoptpet.server.adopt.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ADOPT")
public class Adopt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_no")
    private Integer saleNo;

    @Column(name = "category_no", nullable = false)
    private Integer categoryNo;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "kind", nullable = false, length = 100)
    private String kind;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "latitude", nullable = false)
    private Float latitude;

    @Column(name = "longitude", nullable = false)
    private Float longitude;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AdoptStatus status;

    @Column(name = "reg_date", nullable = false)
    private LocalDateTime regDate;

    @Column(name = "reg_id", nullable = false, length = 50)
    private String regId;

    @Column(name = "mod_date", nullable = false)
    private LocalDateTime modDate;

    @Column(name = "mod_id", nullable = false, length = 50)
    private String modId;

    @Column(name = "visible_yn")
    private String visibleYn;

    @Column(name = "logical_del")
    private Integer logicalDel;

    @Column(name = "blind_yn")
    private Integer blindYn;

}
