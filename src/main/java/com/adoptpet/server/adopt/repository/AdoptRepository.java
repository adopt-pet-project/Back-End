package com.adoptpet.server.adopt.repository;

import com.adoptpet.server.adopt.domain.Adopt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdoptRepository extends JpaRepository<Adopt, Integer> {

    void deleteBySaleNo(Integer saleNo);
}
