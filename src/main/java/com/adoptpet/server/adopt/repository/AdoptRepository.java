package com.adoptpet.server.adopt.repository;

import com.adoptpet.server.adopt.domain.Adopt;
import com.adoptpet.server.adopt.domain.AdoptStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AdoptRepository extends JpaRepository<Adopt, Integer> {


    @Query("delete Adopt a where a.saleNo = :saleNo")
    @Modifying(clearAutomatically = true)
    void deleteBySaleNo(@Param("saleNo") Integer saleNo);

    @Query("update Adopt a set a.viewCnt = a.viewCnt + 1 where a.saleNo = :saleNo")
    @Modifying(clearAutomatically = true)
    @Transactional
    void increaseCount(@Param("saleNo") Integer saleNo);

    @Query("update Adopt a set a.status = :status where a.saleNo = :saleNo")
    @Modifying(clearAutomatically = true)
    void updateAdoptStatus(@Param("status") AdoptStatus status, @Param("saleNo") Integer saleNo);

    @Query("select a from Adopt a where a.regId = :regId and a.saleNo = :saleNo")
    Optional<Adopt> findAdoptIsMine(@Param("regId") String regId, @Param("saleNo") Integer saleNo);


}
