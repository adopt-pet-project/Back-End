package com.adoptpet.server.community.repository;

import com.adoptpet.server.community.domain.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CommunityRepository extends JpaRepository<Community,Integer> {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Query("update Community c set c.viewCount = c.viewCount + 1 where c.articleNo = :articleNo")
    @Modifying(clearAutomatically = true)
    void increaseCount(@Param("articleNo") Integer articleNo);


}
