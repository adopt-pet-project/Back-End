package com.adoptpet.server.community.repository;

import com.adoptpet.server.community.domain.Comment;
import com.adoptpet.server.community.domain.CommentHeart;
import com.adoptpet.server.user.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentHeartRepository extends JpaRepository<CommentHeart, Integer> {
    Optional<CommentHeart> findByCommentAndMember(Comment comment, Member member);

    @Override
    void delete(CommentHeart entity);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM CommentHeart c WHERE c.heartNo = :heartNo")
    void deleteByHeartNo(Integer heartNo);
}
