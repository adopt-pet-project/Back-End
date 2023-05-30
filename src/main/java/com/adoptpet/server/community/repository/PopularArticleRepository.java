package com.adoptpet.server.community.repository;

import com.adoptpet.server.community.domain.PopularArticle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopularArticleRepository extends JpaRepository<PopularArticle,Integer> {
}
