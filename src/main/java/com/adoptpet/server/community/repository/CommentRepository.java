package com.adoptpet.server.community.repository;

import com.adoptpet.server.community.domain.Comment;
import com.adoptpet.server.user.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {

    @Query("SELECT c FROM Comment c WHERE c.community.articleNo = :articleNo")
    List<Comment> findByArticleNo(Integer articleNo);

    @Query("SELECT c FROM Comment c WHERE c.member.memberNo = :memberNo ORDER BY c.commentNo DESC")
    List<Comment> findByMemberNo(Integer memberNo);
}
