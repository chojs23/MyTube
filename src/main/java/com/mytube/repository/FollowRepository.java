package com.mytube.repository;

import com.mytube.domain.Follow;
import com.mytube.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow,Long> {

    Follow findFollowByFromMemberAndToMember(Member fromMember,Member toMember);
}
