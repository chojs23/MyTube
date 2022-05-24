package com.mytube.dto;

import com.mytube.domain.Follow;
import com.mytube.domain.Member;
import com.mytube.domain.Post;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = {"id","userId","password","userEmail"})
public class memberDto {
    private Long id;

    private String userId;

    private String userEmail;

    private String createdDate;

    private String memberImageName;

    private Set<Follow> followings;

    private Set<Follow> followers;

    private List<postDto> posts;


    public memberDto(Member member){
        this.id = member.getId();
        this.userId = member.getUserId();
        this.userEmail = member.getUserEmail();
        this.createdDate = member.getCreatedDate();
        this.memberImageName=member.getMemberImage().getSavedName();
        this.followings = member.getFollowings();
        this.followers = member.getFollowers();

    }

    public memberDto(Member member,List<postDto> posts){
        this.id = member.getId();
        this.userId = member.getUserId();
        this.userEmail = member.getUserEmail();
        this.createdDate = member.getCreatedDate();
        this.memberImageName=member.getMemberImage().getSavedName();
        this.followings = member.getFollowings();
        this.followers = member.getFollowers();
        this.posts=posts;
    }
}
