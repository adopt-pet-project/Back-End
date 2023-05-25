package com.adoptpet.server.community.repository;

import com.adoptpet.server.community.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {

    @Query("SELECT c FROM Comment c WHERE c.community.articleNo = :articleNo")
    List<Comment> findByArticleNo(Integer articleNo);
}
