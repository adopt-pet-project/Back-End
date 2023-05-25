package com.adoptpet.server.community.repository;

import com.adoptpet.server.community.domain.ArticleBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ArticleBookmarkRepository extends JpaRepository<ArticleBookmark,Integer> {

    @Query("select a from ArticleBookmark a where a.member.memberNo = :memberNo and a.community.articleNo = :articleNo")
    Optional<ArticleBookmark> findByMemberNoAndArticleNo(Integer memberNo, Integer articleNo);
}
