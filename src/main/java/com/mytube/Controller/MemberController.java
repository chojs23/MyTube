package com.mytube.Controller;


import com.mytube.Controller.form.MemberJoinForm;
import com.mytube.Controller.form.MemberLoginForm;
import com.mytube.Controller.form.MemberUpdateForm;
import com.mytube.domain.Member;
import com.mytube.dto.memberDto;
import com.mytube.service.MemberService;
import com.mytube.web.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    //@GetMapping("/members")
    public String showMembers(Model model) {
        //List<Member> members = memberService.findMembers();
        //model.addAttribute("members", members);
        return "members/memberList";
    }

    @GetMapping("/members")
    public String showMembers2(@RequestParam Optional<Integer> page,@RequestParam Optional<String> sortBy,Model model) {
        Page<Member> memberPage = memberService.getMemberPage(PageRequest.of(
                page.orElse(0),
                10,
                Sort.Direction.DESC,sortBy.orElse("id")
        ));

        Page<memberDto> members = memberPage.map(m -> new memberDto(m.getId(),m.getUserId(),m.getUserEmail(),m.getCreatedDate()));
        log.info("members ={}", members);
        //List<Member> members = memberPage.getContent();

        model.addAttribute("members",members);
        return "members/memberList";
    }

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("form", new MemberJoinForm());
        return "members/createMemberForm";
    }


    @PostMapping("/members/new")
    public String createMember(@Valid @ModelAttribute("form") MemberJoinForm form, BindingResult result, Model model) {

        if(memberService.checkUserIdDuplication(form)){
            log.info("Duplicated ID ={}",form.getUserId());
            result.addError(new FieldError("form","userId",form.getUserId(),false,null,null,"중복된 ID 입니다."));
        }
        if(memberService.checkUserEmailDuplication(form)){
            log.info("Duplicated Email ={}",form.getUserEmail());
            result.addError(new FieldError("form","userEmail",form.getUserEmail(),false,null,null,"중복된 Email 입니다."));
        }

        if (result.hasErrors()) {
            log.info("errors={}", result);
            return "members/createMemberForm";
        }

        Member member = form.toEntity();

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("members/{id}/update")
    public String updateForm(@PathVariable Long id,Model model,
                             @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember){
        if (!id.equals(loginMember.getId())){
            return "redirect:/";
        }
        Optional<Member> findMember = memberService.findMember(id);
        if(findMember.isEmpty()){
            log.info("update member err");
            return "/home";
        }
        Member member = findMember.get();
        MemberUpdateForm form = new MemberUpdateForm();

        form.setUserId(member.getUserId());
        form.setUserEmail(member.getUserEmail());
        model.addAttribute("form", form);

        return "members/memberUpdateForm";
    }
    @PostMapping("members/{id}/update")
    public String update(@PathVariable Long id,Model model,@ModelAttribute("form") MemberUpdateForm form,BindingResult result,
                             @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember){

        if (!id.equals(loginMember.getId())){
            return "redirect:/";
        }
        if(!loginMember.getUserId().equals(form.getUserId()) && memberService.checkUserIdDuplication(form)){
            log.info("Duplicated ID ={}",form.getUserId());
            result.addError(new FieldError("form","userId",form.getUserId(),false,null,null,"중복된 ID 입니다."));
        }
        if(!loginMember.getUserEmail().equals(form.getUserEmail()) &&memberService.checkUserEmailDuplication(form)){
            log.info("Duplicated Email ={}",form.getUserEmail());
            result.addError(new FieldError("form","userEmail",form.getUserEmail(),false,null,null,"중복된 Email 입니다."));
        }
        if (result.hasErrors()) {
            log.info("errors={}", result);
            return "members/memberUpdateForm";
        }
        boolean b = memberService.updateMember(id, form);

        if (!b){
            log.info("update member err");
            return "members/memberUpdateForm";
        }

        return "redirect:/";
    }

    @GetMapping("members/login")
    public String loginForm(@ModelAttribute("form") MemberLoginForm form) {
        return "members/memberLoginForm";
    }

    @PostMapping("members/login")
    public String login(@Valid @ModelAttribute("form") MemberLoginForm form, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "members/memberLoginForm";
        }
        Member loginMember = memberService.login(form.getUserId(), form.getPassword());
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
