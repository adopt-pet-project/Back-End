package com.adoptpet.server.community.repository;

import com.adoptpet.server.community.domain.PopularArticle;
import com.adoptpet.server.community.domain.PopularEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface PopularArticleRepository extends JpaRepository<PopularArticle,Integer> {

    @Query("SELECT pa FROM PopularArticle pa WHERE pa.regDate BETWEEN :startAt AND :endAt AND pa.status = :status")
    List<PopularArticle> findArticleByPeriod(LocalDateTime startAt, LocalDateTime endAt, PopularEnum status);
}
