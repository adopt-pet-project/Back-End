package com.adoptpet.server.adopt.repository;

import com.adoptpet.server.adopt.domain.AdoptBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface AdoptBookmarkRepository extends JpaRepository<AdoptBookmark, Integer> {

    @Query("select a from AdoptBookmark a where a.member.memberNo = :memberNo and a.adopt.saleNo = :saleNo")
    Optional<AdoptBookmark> findByMemberNoAndSaleNo(Integer memberNo, Integer saleNo);


}
