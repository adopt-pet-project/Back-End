package com.adoptpet.server.community.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import static com.adoptpet.server.community.domain.QNote.note;
import static com.adoptpet.server.community.domain.QNoteHistory.noteHistory;
import static com.adoptpet.server.user.domain.QMember.member;

@Slf4j
@Repository
public class NoteQDslRepository {

    private final JPAQueryFactory query;

    public NoteQDslRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }


}
