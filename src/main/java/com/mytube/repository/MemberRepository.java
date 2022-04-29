package com.mytube.repository;


import com.mytube.domain.Member;
import com.mytube.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByUserId(String userId);
    boolean existsByUserEmail(String userEmail);


    Optional<Member> findById(Long id);

    @Query("select m from Member m where m.userId=:userId")
    Member findMemberByUserId(@Param("userId") String userId);

    @Query(value = "select m from Member m",
            countQuery = "select count(m.userId) from Member m")
    Page<Member> findMemberAllCountBy(Pageable pageable);




}
