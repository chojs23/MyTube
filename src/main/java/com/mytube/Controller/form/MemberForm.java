package com.mytube.Controller.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class MemberForm {
    @NotEmpty(message = "ID 필수")
    private String userId;

    @NotEmpty(message = "Email 필수")
    private String userEmail;
}
