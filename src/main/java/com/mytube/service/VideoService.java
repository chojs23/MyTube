package com.mytube.service;


import com.mytube.domain.Post;
import com.mytube.domain.Video;
import com.mytube.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class VideoService {
    private final VideoRepository videoRepository;

    public Page<Video> getVideoPage(Pageable pageable){
        return videoRepository.findAll(pageable);
    }

    @Transactional
    public Long uploadVideo(Video video){
        log.info("create new video = " + video);
        videoRepository.save(video);
        return video.getId();
    }

    public Video getVideo(Long id){
        return videoRepository.findById(id).get();
    }

}
