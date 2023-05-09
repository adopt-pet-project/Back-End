package com.adoptpet.server.community.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name ="REPORT")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_no")
    private Integer reportNo;

    @Column(name = "report_date", nullable = false)
    private LocalDateTime reportDate;

    @Column(name = "report_id", nullable = false, length = 50)
    private String reportId;

    @Column(name = "target_id", nullable = false, length = 50)
    private String targetId;

    @Column(name = "division", nullable = false, length = 50)
    private String division;

    @Column(name = "content", nullable = false, columnDefinition = "text")
    private String content;

    @Column(name = "status", nullable = false, length = 20, columnDefinition = "varchar(20) default 'UNCONFIRMED'")
    private String status;

    @Column(name = "table_key", nullable = false)
    private Integer tableKey;

    @Column(name = "target", nullable = false, length = 20)
    private String target;
}
