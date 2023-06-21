package com.adoptpet.server.adopt.repository;

import com.adoptpet.server.adopt.domain.AdoptAggregation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface AdoptAggregationRepository extends JpaRepository<AdoptAggregation, Integer> {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Modifying(clearAutomatically = true)
    @Query("update AdoptAggregation a set a.chatCount = a.chatCount - 1 where a.saleNo = :saleNo")
    void decreaseChatCount(@Param("saleNo") Integer saleNo);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Modifying(clearAutomatically = true)
    @Query("update AdoptAggregation a set a.chatCount = a.chatCount + 1 where a.saleNo = :saleNo")
    void increaseChatCount(@Param("saleNo") Integer saleNo);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Modifying(clearAutomatically = true)
    @Query("update AdoptAggregation a set a.bookmarkCount = a.bookmarkCount + 1 where a.saleNo = :saleNo")
    void increaseBookmarkCount(@Param("saleNo") Integer saleNo);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Modifying(clearAutomatically = true)
    @Query("update AdoptAggregation a set a.bookmarkCount = a.bookmarkCount - 1 where a.saleNo = :saleNo")
    void decreaseBookmarkCount(@Param("saleNo") Integer saleNo);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Modifying(clearAutomatically = true)
    @Query("delete AdoptAggregation a where a.saleNo = :saleNo")
    void removeAggregation(@Param("saleNo") Integer saleNo);
}
