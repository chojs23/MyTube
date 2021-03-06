package com.mytube.dto;

import com.mytube.domain.Comment;
import com.mytube.domain.Member;
import com.mytube.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class commentDto {

    private Long id;

    private String comment;

    private Post post;

    private Member member;

    private String createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));




    public commentDto(Comment comment){
        this.id = comment.getId();
        this.comment = comment.getComment();
        this.member = comment.getMember();
        this.post = comment.getPost();
        this.createdDate = comment.getCreatedDate();

    }
}
