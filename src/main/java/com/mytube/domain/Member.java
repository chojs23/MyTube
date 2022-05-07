package com.mytube.domain;


import com.mytube.domain.BaseEntities.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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


    @OneToMany(mappedBy = "member")
    private List<Post> posts=new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Video> videos=new ArrayList<>();

    public void updateMember(String userId,String password,String userEmail,MemberImage memberImage){
        this.userId=userId;
        this.password=password;
        this.userEmail=userEmail;
        this.memberImage=memberImage;
    }

    @Builder
    public Member(String userId, String userEmail,String password,MemberImage memberImage) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.password=password;
        this.memberImage=memberImage;
    }
}
