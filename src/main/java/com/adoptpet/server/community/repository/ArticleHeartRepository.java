package com.adoptpet.server.community.repository;

import com.adoptpet.server.community.domain.ArticleHeart;
import com.adoptpet.server.community.domain.Community;
import com.adoptpet.server.user.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ArticleHeartRepository extends JpaRepository<ArticleHeart,Integer> {

    Optional<ArticleHeart> findByCommunityAndMember(Community community, Member member);

    @Modifying(flushAutomatically = true)
    @Query("DELETE FROM ArticleHeart a  WHERE a.heartNo = :heartNo")
    void deleteByHeartNo(Integer heartNo);
}


