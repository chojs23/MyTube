package com.mytube.service;


import com.mytube.domain.Member;
import com.mytube.domain.Post;
import com.mytube.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PostService {
    private final PostRepository postRepository;

    @Transactional
    public Long createPost(Post post){
        log.info("new post = "+post);
        postRepository.save(post);
        return post.getId();
    }

    public List<Post> getAllPosts(){
        return postRepository.findAll();
    }
    public Page<Post> getPostPage(Pageable pageable){
        return postRepository.findAll(pageable);
    }
}
