package com.mytube.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
public class memberDto {
    private Long id;

    private String userId;

    private String userEmail;

    private String createdDate;


}
