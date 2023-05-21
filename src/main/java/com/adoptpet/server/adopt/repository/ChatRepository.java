package com.adoptpet.server.adopt.repository;

import com.adoptpet.server.adopt.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Integer> {
}
