package com.mytube.dto;

import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import java.time.LocalDateTime;


@Data
public class memberDto {
    private Long id;

    private String userId;

    private String userEmail;

    private LocalDateTime createdDate;

    public memberDto(Long id, String userId, String userEmail, LocalDateTime createdDate) {
        this.id = id;
        this.userId = userId;
        this.userEmail = userEmail;
        this.createdDate = createdDate;
    }
}
