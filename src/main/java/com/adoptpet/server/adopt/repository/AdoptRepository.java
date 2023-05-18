package com.adoptpet.server.adopt.repository;

import com.adoptpet.server.adopt.domain.Adopt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdoptRepository extends JpaRepository<Adopt, Integer> {


    @Query("delete Adopt a where a.saleNo = :saleNo")
    @Modifying(clearAutomatically = true)
    void deleteBySaleNo(@Param("saleNo") Integer saleNo);
}
