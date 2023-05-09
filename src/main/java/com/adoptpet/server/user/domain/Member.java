package com.adoptpet.server.user.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MEMBER")
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_no")
    private Integer memberNo;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "out_yn", nullable = false, columnDefinition = "varchar(10) default 'N'")
    private String outYn;

    @Column(name = "denined_yn", nullable = false, columnDefinition = "varchar(10) default 'N'")
    private String deninedYn;

    @Column(name = "user_role", nullable = false, columnDefinition = "varchar(20) default 'USER'")
    private String userRole;

    @Column(name = "platform", nullable = false)
    private String platform;

    @Column(name = "reg_date", nullable = false)
    private LocalDateTime regDate;

    @Column(name = "nickmod_date", nullable = false)
    private LocalDateTime nickModDate;

    @Column(name = "passmod_date", nullable = false)
    private LocalDateTime passModDate;

}
