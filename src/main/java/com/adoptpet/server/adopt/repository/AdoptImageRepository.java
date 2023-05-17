package com.adoptpet.server.adopt.repository;

import com.adoptpet.server.adopt.domain.AdoptImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface AdoptImageRepository extends JpaRepository<AdoptImage, Integer> {

    @Modifying(clearAutomatically = true)
    @Query("update AdoptImage a set a.saleNo = :saleNo where a.pictureNo = :pictureNo")
    void updateAdoptImageSaleNo(@Param("saleNo") Integer saleNo, @Param("pictureNo") Integer pictureNo);

}