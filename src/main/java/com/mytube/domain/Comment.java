package com.mytube.domain;


import com.mytube.domain.BaseEntities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "comments")
@Entity
public class Comment {
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @CreatedDate
    @Column(name = "created_date")
    private String createdDate;



    @Builder
    public Comment(String comment, Post post, Member member,String createdDate) {
        this.comment = comment;
        this.post=post;
        this.member=member;
        this.createdDate = createdDate;

        this.member.getComments().add(this);
        this.post.getComments().add(this);
    }
    public static Comment createComment(String comment,Member member,Post post,String createdDate){
        return Comment.builder()
                .comment(comment)
                .member(member)
                .post(post)
                .createdDate(createdDate)
                .build();

    }
}
