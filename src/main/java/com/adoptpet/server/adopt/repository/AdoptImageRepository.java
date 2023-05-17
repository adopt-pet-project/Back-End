package com.adoptpet.server.adopt.repository;

import com.adoptpet.server.adopt.domain.AdoptImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface AdoptImageRepository extends JpaRepository<AdoptImage, Integer> {

    @Modifying(clearAutomatically = true)
    @Query("update AdoptImage a set a.saleNo = :saleNo, a.sort = :sort where a.pictureNo = :pictureNo")
    void updateAdoptImageSaleNo(@Param("saleNo") Integer saleNo, @Param("pictureNo") Integer pictureNo, @Param("sort") Integer sort);

    @Modifying(clearAutomatically = true)
    @Query("update AdoptImage a set a.saleNo = null where a.saleNo = :saleNo")
    void updateAdoptImageNull(@Param("saleNo") Integer saleNo);

    @Query("select a.imageUrl from AdoptImage a where a.pictureNo = :pictureNo")
    String findImageUrlByPictureNo(@Param("pictureNo") Integer pictureNo);


}
