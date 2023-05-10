package com.adoptpet.server.user.domain;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Table(name = "MEMBER")
@AllArgsConstructor @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@ToString
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_no")
    private Integer memberNo;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "out_yn")
    private String outYn;

    @Column(name = "denined_yn")
    private String deninedYn;

    @Column(name = "user_role")
    private String userRole;

    @Column(name = "platform")
    private String platform;

//    @Column(name = "reg_date", nullable = false)
//    private LocalDateTime regDate;
//
//    @Column(name = "nickmod_date", nullable = false)
//    private LocalDateTime nickModDate;
//
//    @Column(name = "passmod_date", nullable = false)
//    private LocalDateTime passModDate;

}
