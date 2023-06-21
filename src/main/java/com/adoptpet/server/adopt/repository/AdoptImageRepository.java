package com.adoptpet.server.adopt.repository;

import com.adoptpet.server.adopt.domain.AdoptImage;
import com.adoptpet.server.user.domain.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
 

public interface AdoptImageRepository extends JpaRepository<AdoptImage, Integer> {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Modifying(clearAutomatically = true)
    @Query("update AdoptImage a set a.saleNo = :saleNo, a.sort = :sort where a.pictureNo = :pictureNo")
    void updateAdoptImageSaleNo(@Param("saleNo") Integer saleNo, @Param("pictureNo") Integer pictureNo, @Param("sort") Integer sort);

    @Modifying(clearAutomatically = true)
    @Query("update AdoptImage a set a.saleNo = null where a.saleNo = :saleNo")
    void updateAdoptImageNull(@Param("saleNo") Integer saleNo);

    @Query("select a from AdoptImage a where a.saleNo = null")
    List<AdoptImage> findAllAdoptImageNull();

}
