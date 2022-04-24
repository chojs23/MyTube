package com.mytube.Controller.form;


import com.mytube.domain.Member;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class MemberUpdateForm extends MemberForm{
    @NotEmpty(message = "ID 필수")
    private String userId;
    @NotEmpty(message = "pw 필수")
    private String oldPassword;
    @NotEmpty(message = "pw 필수")
    private String newPassword;
    @NotEmpty(message = "Email 필수")
    private String userEmail;

    public Member toEntity(){
        return Member.builder()
                .userId(userId)
                .password(newPassword)
                .userEmail(userEmail)
                .build();
    }
}
