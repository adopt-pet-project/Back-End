package com.adoptpet.server.adopt.domain;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "CHAT")
@DynamicInsert
@AllArgsConstructor @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_no")
    private Integer chatNo;

    @Column(name = "create_member")
    private Integer createMember;

    @Column(name = "join_member")
    private Integer joinMember;

    @Column(name = "sale_no")
    private Integer saleNo;

    @Column(name = "reg_date")
    private LocalDateTime regDate;


}
