package com.adoptpet.server.user.repository;

import com.adoptpet.server.user.domain.ReportImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportImageRepository extends paRepository<ReportImage, Integer> {

    @Query("select r from ReportImage r where r.declNo = null")
    List<ReportImage> findAllReportImageNull();
}
