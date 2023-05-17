package com.adoptpet.server.adopt.repository;

import com.adoptpet.server.adopt.domain.Adopt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface AdoptRepository extends JpaRepository<Adopt, Integer> {


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Modifying(clearAutomatically = true)
    void deleteBySaleNo(@Param("saleNo") Integer saleNo);
}
