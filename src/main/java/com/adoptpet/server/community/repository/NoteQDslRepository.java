package com.adoptpet.server.community.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Slf4j
@Repository
public class NoteQDslRepository {

    private final JPAQueryFactory query;

    public NoteQDslRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

//
//    public List<NoteListDto> findNoteList(Integer memberNo){
//        query.select(Projections.constructor(NoteListDto.class,
//                note.noteNo,
//                member.nickname,
//
//                ))
//                .from(note)
//                .leftJoin(member)
//                .where();
//
//
//    }
}
