package com.mytube.Controller.form;


import com.mytube.domain.Member;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class MemberJoinForm extends MemberForm{

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String userId;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    //@Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{4,20}",message = "비밀번호는 숫자, 특수기호가 적어도 1개 이상씩 포함된 4자 ~ 20자의 비밀번호여야 합니다.")
    private String password;

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String userEmail;

    public Member toEntity(){
        return Member.builder()
                .userId(userId)
                .password(password)
                .userEmail(userEmail)
                .build();
    }
}
