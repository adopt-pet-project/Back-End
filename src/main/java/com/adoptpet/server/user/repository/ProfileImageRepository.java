package com.adoptpet.server.user.repository;

import com.adoptpet.server.user.domain.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProfileImageRepository extends JpaRepository<ProfileImage,Integer> {

    @Modifying(clearAutomatically = true)
    @Query("update ProfileImage p set p.memberNo = :memberNo where p.pictureNo = :pictureNo")
    void updateProfileImage(@Param("memberNo") Integer memberNo, @Param("pictureNo") Integer pictureNo);

    @Query("select p from ProfileImage p where p.memberNo = null")
    List<ProfileImage> findAllProfileImageNull();
}
