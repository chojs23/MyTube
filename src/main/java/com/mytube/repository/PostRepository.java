package com.mytube.repository;

import com.mytube.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select p from Post p where p.member.id = :id")
    List<Post> findPostsByMember_Id(@Param("id") Long id);

}
