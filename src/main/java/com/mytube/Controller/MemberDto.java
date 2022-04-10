package com.mytube.Controller;


import com.mytube.domain.Member;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

@Data
public class MemberDto {
    @NotEmpty(message = "ID 필수")
    private String userId;
    private String password;
    private String userEmail;

    public Member toEntity(){
        return Member.builder()
                .userId(userId)
                .password(password)
                .userEmail(userEmail)
                .build();
    }
}
