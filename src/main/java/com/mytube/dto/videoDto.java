package com.mytube.dto;


import com.mytube.domain.Member;
import com.mytube.domain.Video;
import com.mytube.domain.VideoFile;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;

@Data
@AllArgsConstructor
public class videoDto {
    private Long id;

    private String title;

    private VideoFile attachFile;

    private Member member;

    private String createdDate;

    private String lastModifiedDate;

    public Video toEntity(){
        return Video.builder()
                .title(title)
                .attachFile(attachFile)
                .member(member)
                .build();
    }
}
