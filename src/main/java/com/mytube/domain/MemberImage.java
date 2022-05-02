package com.mytube.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
public class MemberImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_image_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String origFileName;

    @Column(nullable = false)
    private String savedName;

    @Column(nullable = false)
    private String filePath;

    @Builder
    public MemberImage(Member member, String origFileName,String savedName,String filePath) {
        this.member = member;
        this.origFileName = origFileName;
        this.savedName = savedName;
        this.filePath = filePath;
    }

}