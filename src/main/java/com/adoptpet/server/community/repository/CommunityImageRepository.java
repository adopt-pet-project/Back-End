package com.adoptpet.server.community.repository;

import com.adoptpet.server.community.domain.CommunityImage;
import com.adoptpet.server.user.domain.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommunityImageRepository extends JpaRepository<CommunityImage,Integer> {
    void deleteByArticleNo(Integer articleNo);

    @Query("SELECT c.imageUrl FROM CommunityImage c WHERE c.articleNo = :articleNo ORDER BY c.sort asc ")
    Optional<List<String>> findImageUrlByArticleNo(@Param("articleNo") Integer articleNo);

    @Modifying(clearAutomatically = true)
    @Query("update CommunityImage c set c.articleNo = :articleNo, c.sort = :sort where c.pictureNo = :pictureNo")
    void updateImagByArticleNo(@Param("articleNo") Integer articleNo, @Param("pictureNo") Integer pictureNo, @Param("sort") Integer sort);

    @Modifying(clearAutomatically = true)
    @Query("update CommunityImage cc set c.articleNo=null where c.articleNo = :articleNo")
    void updateAllByArticleNo(@Param("articleNo") Integer articleNo);

    @Query("select c from CommunityImage c where c.articleNo = null")
    List<CommunityImage> findAllCommunityImageNull();
}
