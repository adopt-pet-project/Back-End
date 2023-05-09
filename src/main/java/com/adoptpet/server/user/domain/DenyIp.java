package com.adoptpet.server.user.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "DENY_IP")
public class DenyIp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deny_no")
    private Integer denyNo;

    @Column(name = "deny_ip", nullable = false, length = 200)
    private String denyIp;

    @Column(name = "reg_date", nullable = false)
    private LocalDateTime regDate;

    @Column(name = "reg_id", nullable = false, length = 50)
    private String regId;
}
