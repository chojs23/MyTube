package com.mytube.domain;

import com.mytube.domain.BaseEntities.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
@ToString(of = {"id","member","origFileName"})
public class MemberImage{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_image_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //@Column(nullable = false)
    private String origFileName;

    //@Column(nullable = false)
    private String savedName;

    //@Column(nullable = false)
    private String filePath;

    @Builder
    public MemberImage(Member member, String origFileName,String savedName,String filePath) {
        this.member = member;
        this.origFileName = origFileName;
        this.savedName = savedName;
        this.filePath = filePath;
    }
    public void updateMemberImage(String origFileName,String savedName,String filePath){
        this.origFileName = origFileName;
        this.savedName = savedName;
        this.filePath = filePath;
    }

}