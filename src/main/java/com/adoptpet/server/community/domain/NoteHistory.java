package com.adoptpet.server.community.domain;

import com.adoptpet.server.commons.support.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.C;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "NOTE_HISTORY")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoteHistory extends BaseTimeEntity {

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

    @Column(name = "read_status")
    private boolean isRead;

    @Column(name = "receiver_del")
    private LogicalDelEnum receiverDel;

    @Column(name = "sender_del")
    private LogicalDelEnum senderDel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_no")
    private Note note;

    @Builder
    public NoteHistory(Integer senderNo, Integer receiverNo, String content, boolean isRead, LogicalDelEnum receiverDel, LogicalDelEnum senderDel) {
        this.senderNo = senderNo;
        this.receiverNo = receiverNo;
        this.content = content;
        this.isRead = isRead;
        this.receiverDel = receiverDel;
        this.senderDel = senderDel;
    }


    public static NoteHistory createNoteHistory(Integer senderNo,Integer receiverNo, String content){
        return NoteHistory.builder()
                .senderNo(senderNo)
                .receiverNo(receiverNo)
                .content(content)
                .isRead(false)
                .receiverDel(LogicalDelEnum.NORMAL)
                .senderDel(LogicalDelEnum.NORMAL)
                .build();
    }


    public void addNote(Note note) {
        this.note = note;
    }

    //== 삭제 로직 ==//
    public void softDeleteNoteHistory(Integer memberNo,LogicalDelEnum logicalDel){
        if(memberNo.equals(this.senderNo)){
            this.senderDel = logicalDel;
        } else {
            this.receiverDel = logicalDel;
        }
    }
}
