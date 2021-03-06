package com.mytube.service;


import com.mytube.Controller.form.MemberUpdateForm;
import com.mytube.Controller.form.PostCreateForm;
import com.mytube.domain.Member;
import com.mytube.domain.Post;
import com.mytube.exception.PostNotFoundException;
import com.mytube.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    @Transactional
    public boolean updatePost(Long id, PostCreateForm form){
        Optional<Post> findPost = postRepository.findById(id);

        if(findPost.isEmpty()){
            throw new PostNotFoundException("There is no post id = " + id);
        }
        Post post = findPost.get();
        log.info("post = " + post);

        post.updatePost(form.getTitle(), form.getContent());
        Post save = postRepository.save(post);
        log.info("save = " + save);
        return true;
    }

    @Transactional
    public Post deletePost(Long id){
        Optional<Post> findPost = postRepository.findById(id);
        if(!findPost.isPresent()){
            log.info("isPresent false");
            return null;
        }
        Post post = findPost.get();

        postRepository.delete(post);

        return post;
    }

    public Post getPost(Long id){
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            throw new PostNotFoundException(String.format("There is no post id = %s", id));
        }
        return post.get();
    }

    @Transactional
    public Page<Post> search(String keyword,Pageable pageable) {
        Page<Post> postsList = postRepository.findByTitleContaining(keyword,pageable);
        return postsList;
    }

    public List<Post> getPostsFromMember(Long id){
        return postRepository.findPostsByMember_Id(id);
    }

    public List<Post> getAllPosts(){
        return postRepository.findAll();
    }

    public Page<Post> getPostPage(Pageable pageable){
        return postRepository.findAll(pageable);
    }

    public Page<Post> getFollowingPosts(Long id,Pageable pageable) {
        return postRepository.findFollowingPosts(id,pageable);
    }

    public Page<Post> getPostPage(String keyword,Pageable pageable){
        if (keyword.isEmpty()) {
            return postRepository.findAll(pageable);
        }
        else{
            return postRepository.findByTitleContaining(keyword, pageable);
        }
    }
}
