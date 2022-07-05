package com.mytube.domain;


import com.mytube.domain.BaseEntities.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"title","member","createdDate","lastModifiedDate"})
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false,length = 1000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @OneToMany(mappedBy = "post",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Comment> comments=new ArrayList<>();


    public void setMember(Member member){
        this.member = member;
        this.member.getPosts().add(this);
    }
    public void updatePost(String title,String content){
        this.title=title;
        this.content=content;
    }

    @Builder
    public Post(String title, String content,Member member) {
        this.title=title;
        this.content=content;
        this.member=member;

        this.member.getPosts().add(this);

    }

    public static Post createPost(String title, String content, Member member) {
        return builder()
                .title(title)
                .content(content)
                .member(member)
                .build();
    }

}
