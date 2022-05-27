package com.mytube.service;


import com.mytube.domain.Comment;
import com.mytube.domain.Member;
import com.mytube.domain.Post;
import com.mytube.dto.commentDto;
import com.mytube.exception.MemberNotFoundException;
import com.mytube.repository.CommentRepository;
import com.mytube.repository.MemberRepository;
import com.mytube.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
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
                new IllegalArgumentException("addComment Error : There are no posts id with "+id));

        dto.setMember(member);
        dto.setPost(post);

        Comment comment = dto.toEntity();
        commentRepository.save(comment);

        return dto.getId();
    }
}
