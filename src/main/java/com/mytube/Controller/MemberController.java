package com.mytube.Controller;


import com.mytube.domain.Member;
import com.mytube.service.memberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final memberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberDto", new MemberDto());
        return "members/createMemberForm";
    }

    @GetMapping("/members")
    public String showMembers(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
    @PostMapping("/members/new")
    public String createMember(@Valid MemberDto dto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        try {
            memberService.checkUserIdDuplication(dto);
        } catch (IllegalStateException e) {
            model.addAttribute("DuplicateUserId", true);
            return "members/createMemberForm";
        }

        try {
            memberService.checkUserEmailDuplication(dto);
        } catch (IllegalStateException e) {
            model.addAttribute("DuplicateUserEmail", true);
            return "members/createMemberForm";
        }

        Member member = dto.toEntity();
        memberService.join(member);
        return "redirect:/";
    }


}
