package com.mytube.domain;


import com.mytube.domain.BaseEntities.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "videos")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"title","member","createdDate"})
public class Video extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "video_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Embedded
    private VideoFile attachFile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;



    @Builder
    public Video(String title, VideoFile attachFile, Member member) {
        this.title=title;
        this.attachFile=attachFile;
        this.member = member;

    }
}
