package com.mytube.dto;

import com.mytube.domain.Comment;
import com.mytube.domain.Member;
import com.mytube.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class postDto {

    private Long id;

    private String title;

    private String content;

    private Member member;

    private List<commentDto> comments;

    private String createdDate;

    private String lastModifiedDate;

    public postDto(Post post){
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.member = post.getMember();
        this.comments=post.getComments().stream().map(comment -> new commentDto(comment)).collect(Collectors.toList());
        this.createdDate = post.getCreatedDate();
        this.lastModifiedDate = post.getLastModifiedDate();
    }

}
