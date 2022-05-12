package com.mytube.Controller;


import com.mytube.Controller.form.MemberForm;
import com.mytube.Controller.form.MemberJoinForm;
import com.mytube.Controller.form.MemberLoginForm;
import com.mytube.Controller.form.MemberUpdateForm;
import com.mytube.domain.Member;
import com.mytube.domain.MemberImage;
import com.mytube.domain.Post;
import com.mytube.dto.memberDto;
import com.mytube.dto.postDto;
import com.mytube.service.MemberService;
import com.mytube.service.PostService;
import com.mytube.upload.UploadImage;
import com.mytube.web.SessionConst;
import com.mytube.web.validator.MemberUpdateFormValidator;
import com.mytube.web.validator.SignUpFormValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.awt.print.Pageable;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final SignUpFormValidator signUpFormValidator;
    private final MemberUpdateFormValidator memberUpdateFormValidator;

    private final MemberService memberService;
    private final PostService postService;
    private final UploadImage uploadImage;
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
    public String createMember(@Valid @ModelAttribute("form") MemberJoinForm form, Errors errors, Model model) {
//        if(memberService.checkUserIdDuplication(form.getUserId())){
//            log.info("Duplicated ID ={}",form.getUserId());
//            result.addError(new FieldError("form","userId",form.getUserId(),false,null,null,"중복된 ID 입니다."));
//        }
//        if(memberService.checkUserEmailDuplication(form.getUserEmail())){
//            log.info("Duplicated Email ={}",form.getUserEmail());
//            result.addError(new FieldError("form","userEmail",form.getUserEmail(),false,null,null,"중복된 Email 입니다."));
//        }
        signUpFormValidator.validate(form, errors);
        if (errors.hasErrors()) {
            log.info("errors={}", errors);
            return "members/createMemberForm";
        }

        Member member = form.toEntity();

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("members/{id}/info")
    public String info(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model,
                       @PathVariable Long id) throws MalformedURLException {
        if (!id.equals(loginMember.getId())){
            return "redirect:/";
        }
        List<Post> postsFromMember = postService.getPostsFromMember(id);

        List<postDto> MemberPostsDto = postsFromMember.stream().map(postDto::new).collect(Collectors.toList());

        MemberImage memberImage = loginMember.getMemberImage();
        String filename = null;
        if (memberImage != null) {
            filename = memberImage.getSavedName();
        }
        MemberForm memberForm = new MemberForm(loginMember.getUserId(), loginMember.getUserEmail(), MemberPostsDto, filename);

        log.info("memberForm "+ memberForm);
        model.addAttribute("form",memberForm);
        model.addAttribute("loginMember",loginMember);
        return "/members/memberInfo";
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource showMemberImage(@PathVariable String filename) throws
            MalformedURLException {
        return new UrlResource("file:" + uploadImage.getFullPath(filename));
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

    /**
     * 사용자가 자신의 정보를 수정하면 세션에 유지중인 loginMember 에는 값이 반영안됨
     * 그래서 또 다음 수정할때 오류발생
     * 해결 -> 세션에 유지중인 loginMember 을 수정된 새로운 Member 로 갱신
     * Or 그냥 수정하면 로그아웃시키기 ?
     *
     *
     * 아이디 이메일 중복검사, 비번확인 따로해서 BindingResult에 넣었는데 @Validation 커스텀으로 새로 만들기로 가능
     *
     */
    @PostMapping("members/{id}/update")
    public String update(@PathVariable Long id, Model model, @Valid @ModelAttribute("form") MemberUpdateForm form, Errors errors,
                         @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                         HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException {

        if (!id.equals(loginMember.getId())){
            return "redirect:/";
        }

//        if(!loginMember.getUserId().equals(form.getUserId()) && memberService.checkUserIdDuplication(form.getUserId())){
//            log.info("Duplicated ID ={}",form.getUserId());
//            result.addError(new FieldError("form","userId",form.getUserId(),false,null,null,"중복된 ID 입니다."));
//        }
//        if(!loginMember.getUserEmail().equals(form.getUserEmail()) &&memberService.checkUserEmailDuplication(form.getUserEmail())){
//            log.info("Duplicated Email ={}",form.getUserEmail());
//            result.addError(new FieldError("form","userEmail",form.getUserEmail(),false,null,null,"중복된 Email 입니다."));
//        }
//        if(!loginMember.getPassword().equals(form.getOldPassword())){
//            log.info("OldPassword Error ={}",form.getOldPassword());
//            errors.addError(new FieldError("form","oldPassword",form.getOldPassword(),false,null,null,"기존 비밀번호를 잘못입력하였습니다."));
//        }
        form.setOldUserId(loginMember.getUserId());
        form.setOldUserEmail(loginMember.getUserEmail());
        form.setLoginMemberPassword(loginMember.getPassword());

        memberUpdateFormValidator.validate(form, errors);
        if (errors.hasErrors()) {
            log.info("errors={}", errors);
            return "members/memberUpdateForm";
        }

        MemberImage memberImage = uploadImage.uploadMemberImage(form.getMultipartFile(), id);

        form.setMemberImage(memberImage);

        boolean b = memberService.updateMember(id, form);

        if (!b){
            log.info("update member err");
            return "members/memberUpdateForm";
        }

        HttpSession session = request.getSession();
        //세션에 로그인 회원 정보 보관

        session.setAttribute(SessionConst.LOGIN_MEMBER, memberService.findMember(loginMember.getId()).get());

        redirectAttributes.addAttribute("id", id);

        return "redirect:/members/{id}/info";
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
    public String logout(HttpServletRequest request) {
        //세션을 삭제한다.
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

}
