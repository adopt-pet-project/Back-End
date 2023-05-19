package com.adoptpet.server.user.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MEMBER_DENINED")
public class MemberDenied {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "denined_no")
    private Integer deninedNo;

    @Column(name = "member_no", nullable = false)
    private Integer memberNo;

    @Column(name = "denined_reason", nullable = false, length = 200)
    private String deninedReason;

    @Column(name = "denined_date", nullable = false)
    private LocalDateTime deninedDate;

    @Column(name = "exp_denined_date", nullable = false)
    private LocalDateTime expDeninedDate;

    @Column(name = "reg_date", nullable = false)
    private LocalDateTime regDate;

    @Column(name = "mod_date", nullable = false)
    private LocalDateTime modDate;

    @Column(name = "reg_id", nullable = false, length = 50)
    private String regId;

    @Column(name = "mod_id", nullable = false, length = 50)
    private String modId;
}
