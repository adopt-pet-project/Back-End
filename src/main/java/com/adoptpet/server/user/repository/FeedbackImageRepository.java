package com.adoptpet.server.user.repository;

import com.adoptpet.server.user.domain.FeedbackImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedbackImageRepository extends JpaRepository<FeedbackImage, Integer> {

    @Query("select f from FeedbackImage f where f.feedbackNo = null")
    List<FeedbackImage> findAllFeedbackImageNull();
}
