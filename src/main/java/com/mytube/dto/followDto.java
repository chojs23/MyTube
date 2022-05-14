package com.mytube.dto;

import com.mytube.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class followDto {
    private String fromMemberUserId;
    private String toMemberUserId;

}
