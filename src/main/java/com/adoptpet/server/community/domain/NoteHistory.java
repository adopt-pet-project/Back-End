package com.adoptpet.server.community.domain;

import lombok.AccessLevel;
import lombok.Builder;
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

    @Column(name = "sender_no")
    private Integer senderNo;

    @Column(name = "receiver_no")
    private Integer receiverNo;

    @Column(name = "content")
    private String content;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Column(name = "mod_date")
    private LocalDateTime modDate;

    @Column(name = "receiver_del")
    private LogicalDelEnum receiverDel;

    @Column(name = "sender_del")
    private LogicalDelEnum senderDel;

    @Column(name = "read_status")
    private boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_no")
    private Note note;


    @Builder
    public NoteHistory(Integer receiverNo, Integer senderNo, String content, LocalDateTime regDate, LocalDateTime modDate, LogicalDelEnum receiverDel, LogicalDelEnum senderDel, boolean isRead) {
        this.receiverNo = receiverNo;
        this.senderNo = senderNo;
        this.content = content;
        this.regDate = regDate;
        this.modDate = modDate;
        this.receiverDel = receiverDel;
        this.senderDel = senderDel;
        this.isRead = isRead;
    }


    public static NoteHistory createNoteHistory(Integer senderNo,Integer receiverNo, String content){
        return NoteHistory.builder()
                .senderNo(senderNo)
                .receiverNo(receiverNo)
                .content(content)
                .regDate(LocalDateTime.now())
                .modDate(LocalDateTime.now())
                .receiverDel(LogicalDelEnum.NORMAL)
                .senderDel(LogicalDelEnum.NORMAL)
                .isRead(false)
                .build();
    }

    public void addNote(Note note) {
        this.note = note;
    }
}
