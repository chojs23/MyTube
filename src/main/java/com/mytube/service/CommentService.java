package com.mytube.service;


import com.mytube.domain.Comment;
import com.mytube.domain.Member;
import com.mytube.domain.Post;
import com.mytube.dto.commentDto;
import com.mytube.exception.MemberNotFoundException;
import com.mytube.exception.PostNotFoundException;
import com.mytube.repository.CommentRepository;
import com.mytube.repository.MemberRepository;
import com.mytube.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional
    public Long addComment(String userId, Long id, commentDto dto) {
        Member member = memberRepository.findMemberByUserId(userId).orElseThrow(()->
                new MemberNotFoundException("ex"));
        Post post = postRepository.findById(id).orElseThrow(()->
                new PostNotFoundException("addComment Error : There are no posts id with "+id));


        Comment comment = Comment.createComment(dto.getComment(), member, post,dto.getCreatedDate());
        commentRepository.save(comment);

        return dto.getId();
    }
}
