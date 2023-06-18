package com.adoptpet.server.community.repository;

import com.adoptpet.server.community.domain.Comment;
import com.adoptpet.server.user.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {

    @Query("SELECT c FROM Comment c WHERE c.community.articleNo = :articleNo")
    List<Comment> findByArticleNo(Integer articleNo);

    @Query("SELECT c FROM Comment c WHERE c.member.memberNo = :memberNo AND c.logicalDel = 0 " +
            "ORDER BY c.commentNo DESC")
    List<Comment> findByMemberNo(Integer memberNo);

    @Modifying(clearAutomatically = true)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Query("UPDATE Comment c SET c.logicalDel = 2 , c.member.memberNo = 0 WHERE c.member.memberNo = :memberNo")
    void deleteComment(@Param("memberNo") Integer memberNo);

}
