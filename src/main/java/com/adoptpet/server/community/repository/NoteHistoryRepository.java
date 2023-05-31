package com.adoptpet.server.community.repository;

import com.adoptpet.server.community.domain.NoteHistory;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NoteHistoryRepository extends JpaRepository<NoteHistory, Integer> {
}
