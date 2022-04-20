package com.mytube.Controller;


import com.mytube.Controller.form.MemberJoinForm;
import com.mytube.Controller.form.PostCreateForm;
import com.mytube.domain.Member;
import com.mytube.domain.Post;
import com.mytube.repository.PostRepository;
import com.mytube.service.PostService;
import com.mytube.web.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepository;
    private final PostService postService;

    @GetMapping("/posts")
    public String posts(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,Model model){
        model.addAttribute("member", loginMember);
        return "/posts/posts";
    }

    @GetMapping("/posts/new")
    public String createPost(Model model){
        model.addAttribute("form", new PostCreateForm());

        return "/posts/createPostForm";
    }

    @PostMapping("/posts/new")
    public String createPost(@Valid @ModelAttribute PostCreateForm form, BindingResult result, Model model,
                             @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember){

        if (result.hasErrors()) {
            log.info("errors={}", result);
            return "/posts/createPostForm";
        }

        Post post = form.toEntity(form.getTitle(), form.getContent(), loginMember);
        Long postId = postService.createPost(post);
        log.info("post id = " + postId);
        return "redirect:/posts";
    }

}
