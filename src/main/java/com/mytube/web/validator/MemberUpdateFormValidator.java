package com.mytube.web.validator;

import com.mytube.Controller.form.MemberJoinForm;
import com.mytube.Controller.form.MemberUpdateForm;
import com.mytube.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component // (2)
@RequiredArgsConstructor // (2)
public class MemberUpdateFormValidator implements Validator { // (1)

    private final MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> clazz) { // (3)
        return clazz.isAssignableFrom(MemberUpdateForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) { // (4)
        MemberUpdateForm memberUpdateForm = (MemberUpdateForm) target;
        if (!memberUpdateForm.getOldUserId().equals(memberUpdateForm.getUserId()) &&memberRepository.existsByUserId(memberUpdateForm.getUserId())) {
            errors.rejectValue("userId", "invalid.Id", new Object[]{memberUpdateForm.getUserId()},
                    "이미 사용중인 ID 입니다.");
        }
        if (!memberUpdateForm.getOldUserEmail().equals(memberUpdateForm.getUserEmail()) &&memberRepository.existsByUserEmail(memberUpdateForm.getUserEmail())) {
            errors.rejectValue("userEmail", "invalid.Email", new Object[]{memberUpdateForm.getUserEmail()},
                    "이미 사용중인 이메일입니다.");
        }
        if (!memberUpdateForm.getLoginMemberPassword().equals(memberUpdateForm.getOldPassword())) {
            errors.rejectValue("oldPassword", "invalid.oldPassword", new Object[]{memberUpdateForm.getOldPassword()},
                    "이전 비밀번호가 맞지 않습니다.");
        }


    }
}