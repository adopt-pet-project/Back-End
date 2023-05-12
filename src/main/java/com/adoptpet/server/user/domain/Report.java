package com.adoptpet.server.user.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name ="REPORT")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_no")
    private Integer reportNo;

    @Column(name = "report_date")
    private LocalDateTime reportDate;

    @Column(name = "report_id")
    private String reportId;

    @Column(name = "target_id")
    private String targetId;

    @Column(name = "division")
    private String division;

    @Column(name = "content")
    private String content;

    @Column(name = "status")
    private String status;

    @Column(name = "table_key")
    private Integer tableKey;

    @Column(name = "target")
    private String target;
}
