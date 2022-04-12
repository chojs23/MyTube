package com.mytube.Controller;


import com.mytube.domain.Member;
import com.mytube.service.memberService;
import com.mytube.web.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

    @GetMapping("members/login")
    public String loginForm(@ModelAttribute("LoginDto") LoginDto dto) {
        return "members/memberLoginForm";
    }

    @PostMapping("members/login")
    public String login(@Valid @ModelAttribute LoginDto dto, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "members/memberLoginForm";
        }
        Member loginMember = memberService.login(dto.getMemberId(), dto.getPassword());
        log.info("login? {}", loginMember);
        if (loginMember == null) {
            log.info("null login");
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "members/memberLoginForm";
        }
        //로그인 성공 처리 TODO

        //세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성
        HttpSession session = request.getSession();
        //세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "redirect:/";
    }
    @PostMapping("/members/logout")
    public String logoutV3(HttpServletRequest request) {
        //세션을 삭제한다.
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

}
