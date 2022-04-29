package com.mytube.dto;

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


}
