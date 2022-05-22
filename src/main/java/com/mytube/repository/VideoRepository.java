package com.mytube.repository;

import com.mytube.domain.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video,Long> {


    Page<Video> findByTitleContaining(String keyword, Pageable pageable);

}
