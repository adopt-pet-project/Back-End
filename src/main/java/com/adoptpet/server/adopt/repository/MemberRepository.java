package com.adoptpet.server.adopt.repository;

import com.adoptpet.server.user.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
