package com.adoptpet.server.community.repository;

import com.adoptpet.server.community.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Integer> {
}
