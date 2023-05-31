package com.adoptpet.server.community.domain;

import lombok.AccessLevel;
import lombok.Builder;
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

    @OneToMany(mappedBy = "note", cascade = CascadeType.PERSIST)
    private List<NoteHistory> noteHistoryList = new ArrayList<>();

    @Builder
    public Note(Integer noteNo, Integer createMember, Integer joinMember, LocalDateTime regDate, LocalDateTime updateDate) {
        this.noteNo = noteNo;
        this.createMember = createMember;
        this.joinMember = joinMember;
        this.regDate = regDate;
        this.updateDate = updateDate;
    }


    //== 연관관계 메서드==//
    public void addNoteHistory(NoteHistory noteHistory){
        noteHistoryList.add(noteHistory);
        noteHistory.addNote(this);
    }


    //== 생성 로직 ==//
    public static Note createNote(Integer createMember, Integer joinMember, NoteHistory noteHistory){

        Note note = Note.builder()
                .createMember(createMember)
                .joinMember(joinMember)
                .regDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        note.addNoteHistory(noteHistory);

        return note;
    }
}
