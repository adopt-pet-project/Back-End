package com.adoptpet.server.adopt.repository;

import com.adoptpet.server.adopt.domain.AdoptBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdoptBookmarkRepository extends JpaRepository<AdoptBookmark, Integer> {

    @Query("select count(*) from AdoptBookmark a where a.adopt.saleNo = :saleNo")
    long countAdoptBookmark(@Param("saleNo") Integer saleNo);
}
