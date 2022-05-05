package com.mytube.web.validator;

import com.mytube.Controller.form.MemberForm;
import com.mytube.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@RequiredArgsConstructor
@Component
public class CheckUserIdValidator extends AbstractValidator<MemberForm> {
    private final MemberRepository memberRepository;
    @Override
    protected void doValidate(MemberForm form, Errors errors) {
        if (memberRepository.existsByUserId(form.getUserId())) {
            errors.rejectValue("userId", "아이디 중복 오류", "이미 사용중인 아이디 입니다.");
        }
    }

}
