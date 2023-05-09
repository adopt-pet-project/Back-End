package com.adoptpet.server.community.repository;

import com.adoptpet.server.community.domain.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRepository extends JpaRepository<Community,Integer> {
}
