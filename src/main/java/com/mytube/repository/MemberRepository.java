package com.mytube.repository;


import com.mytube.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByUserId(String userId);
    boolean existsByUserEmail(String userEmail);

    @Query("select m from Member m where m.userId=:userId")
    Member findMemberByUserId(@Param("userId") String userId);
}
