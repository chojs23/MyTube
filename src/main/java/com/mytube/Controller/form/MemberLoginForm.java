package com.mytube.Controller.form;


import com.mytube.domain.Member;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class MemberLoginForm {
    @NotEmpty(message = "ID 필수")
    private String userId;
    @NotEmpty(message = "pw 필수")
    private String password;


    public Member toEntity(){
        return Member.builder()
                .userId(userId)
                .password(password)
                .build();
    }
}
