package com.adoptpet.server.community.repository;

import com.adoptpet.server.community.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
}
