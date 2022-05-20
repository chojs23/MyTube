package com.mytube.repository;

import com.mytube.domain.MemberImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberImageRepository extends JpaRepository<MemberImage,Long> {


    Optional<MemberImage> findMemberImageByMember_Id(Long id);


}
