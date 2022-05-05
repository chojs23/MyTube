package com.mytube.web.validator;

import com.mytube.Controller.form.MemberJoinForm;
import com.mytube.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component // (2)
@RequiredArgsConstructor // (2)
public class SignUpFormValidator implements Validator { // (1)

    private final MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> clazz) { // (3)
        return clazz.isAssignableFrom(MemberJoinForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) { // (4)
        MemberJoinForm signUpForm = (MemberJoinForm) target;
        if (memberRepository.existsByUserId(signUpForm.getUserId())) {
            errors.rejectValue("userId", "invalid.Id", new Object[]{signUpForm.getUserId()},
                    "이미 사용중인 ID 입니다.");
        }
        if (memberRepository.existsByUserEmail(signUpForm.getUserEmail())) {
            errors.rejectValue("userEmail", "invalid.Email", new Object[]{signUpForm.getUserEmail()},
                    "이미 사용중인 이메일입니다.");
        }

    }
}