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

    @OneToMany(mappedBy = "member")
    private List<Post> posts=new ArrayList<>();


    @Builder
    public Member(String userId, String userEmail,String password) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.password=password;
    }
}
