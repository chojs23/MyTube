package com.mytube.dto;

import com.mytube.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class memberDto {
    private Long id;

    private String userId;

    private String userEmail;

    private String createdDate;

    public memberDto(Member member){
        this.id = member.getId();
        this.userId = member.getUserId();
        this.userEmail = member.getUserEmail();
        this.createdDate = member.getCreatedDate();
    }
}
