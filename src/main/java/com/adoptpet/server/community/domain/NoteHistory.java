package com.adoptpet.server.community.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "NOTE_HISTORY")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoteHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_no")
    private Integer historyNo;

    @Column(name = "receiver_no")
    private Integer receiverNo;

    @Column(name = "sender_no")
    private Integer senderNo;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Column(name = "mod_date")
    private LocalDateTime modeDate;

    @Column(name = "receiver_del")
    private LogicalDelEnum receiverDel;

    @Column(name = "sender_del")
    private LogicalDelEnum senderDel;

    @Column(name = "read_status")
    private boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_no")
    private Note note;
}
