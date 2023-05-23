package com.adoptpet.server.community.repository;

import com.adoptpet.server.community.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
}
