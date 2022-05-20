package com.mytube.dto;

import com.mytube.domain.Member;
import com.mytube.domain.Post;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = {"id","userId","password","userEmail"})
public class memberDto {
    private Long id;

    private String userId;

    private String userEmail;

    private String createdDate;

    private List<Post> posts;

    public memberDto(Member member){
        this.id = member.getId();
        this.userId = member.getUserId();
        this.userEmail = member.getUserEmail();
        this.createdDate = member.getCreatedDate();
        this.posts = member.getPosts();
    }
}
