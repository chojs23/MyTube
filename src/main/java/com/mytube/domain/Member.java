package com.mytube.domain;


import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id","userId","password","userEmail"})
public class Member {

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


    @Builder
    public Member(String userId, String userEmail,String password) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.password=password;
    }
}
