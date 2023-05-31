package com.adoptpet.server.community.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "NOTE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Note {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "note_no")
    private Integer noteNo;

    @Column(name = "create_member")
    private Integer createMember;

    @Column(name = "join_member")
    private Integer joinMember;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "note")
    private List<NoteHistory> noteHistoryList = new ArrayList<>();
}
