package com.mytube.Controller;


import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginDto {
    @NotEmpty
    private String memberId;
    @NotEmpty
    private String password;
}
