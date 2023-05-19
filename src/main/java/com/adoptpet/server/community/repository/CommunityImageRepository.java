package com.adoptpet.server.community.repository;

import com.adoptpet.server.community.domain.CommunityImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommunityImageRepository extends JpaRepository<CommunityImage,Integer> {
    void deleteByArticleNo(Integer articleNo);

    @Query("SELECT c.imageUrl FROM CommunityImage c WHERE c.articleNo =: articleNo")
    List<String> findImageUrlByArticleNo(@Param("articleNo") Integer articleNo);
}
