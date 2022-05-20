package com.mytube.domain;


import com.mytube.domain.BaseEntities.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id","userId","password","userEmail"})
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    @Column(nullable = false,unique = true)
    private String userId;
    @Column(nullable = false, length = 15)
    private String password;
    @Column(nullable = false,unique = true,length =45)
    private String userEmail;

    @OneToOne(mappedBy = "member",cascade = CascadeType.ALL)
    private MemberImage memberImage;


    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL)
    private List<Post> posts=new ArrayList<>();

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL)
    private List<Video> videos=new ArrayList<>();

    @OneToMany(mappedBy = "fromMember",cascade = CascadeType.ALL)
    private Set<Follow> followings = new HashSet<>();

    @OneToMany(mappedBy = "toMember",cascade = CascadeType.ALL)
    private Set<Follow> followers = new HashSet<>();

    public void updateMember(String userId,String password,String userEmail,MemberImage memberImage){
        this.userId=userId;
        this.password=password;
        this.userEmail=userEmail;
        this.memberImage=memberImage;
    }
    public void updateMember(String userId,String password,String userEmail){
        this.userId=userId;
        this.password=password;
        this.userEmail=userEmail;

    }

    @Builder
    public Member(String userId, String userEmail,String password,MemberImage memberImage) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.password=password;
        this.memberImage=memberImage;

    }
}
