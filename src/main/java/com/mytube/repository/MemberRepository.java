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

    //TODO countQuery 만들어줘야됨 -> why?

    /**
     * 페이징을 하기 위해서는 전체 카운트가 꼭 있어야한다. 그래야 몇 page까지 있는지 알 수 있다.
     * 그래서 countQuery가 없으면 Spring Data JPA가 임의로 원본 쿼리를 보고 countQuery를 작성하는데, 이때 쿼리에 페치 조인이 들어가게 된다.
     * 페치 조인은 객체 그래프를 조회하는 기능이기 때문에 연관된 부모가 반드시 있어야하나, countQuery의 경우 count(u)로 조회 결과가 변경되었기 때문에 오류가 발생한 것이다.
     *
     * 따라서 fetch join이나 복잡한 쿼리의 경우 반드시 countQuery를 분리해서 사용하자.
     * https://www.inflearn.com/questions/62217
     */
    @Query(value = "SELECT DISTINCT m FROM Member m left join fetch m.memberImage",
            countQuery = " select count(m) from Member m left join m.memberImage")
    Page<Member> findAllMemberPage(Pageable pageable);

    @Query("select m from Member m join fetch m.memberImage mi where m.id=:id")
    Optional<Member> findById(@Param("id") Long id);

    @Query("select m from Member m join fetch m.memberImage mi where m.userId=:userId")
    Optional<Member> findMemberByUserId(@Param("userId") String userId);






}
