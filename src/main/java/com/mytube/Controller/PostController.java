package com.mytube.Controller;


import com.mytube.Controller.form.MemberJoinForm;
import com.mytube.Controller.form.PostCreateForm;
import com.mytube.domain.Member;
import com.mytube.domain.Post;
import com.mytube.dto.postDto;
import com.mytube.repository.PostRepository;
import com.mytube.service.PostService;
import com.mytube.web.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PostController {


    private final PostService postService;

    @GetMapping("/posts")
    public String posts(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model,
                        @RequestParam Optional<Integer> page,@RequestParam Optional<String> sortBy){
        model.addAttribute("loginMember", loginMember);

        Page<Post> postPage = postService.getPostPage(PageRequest.of(
                page.orElse(0),
                10,
                Sort.Direction.DESC,sortBy.orElse("createdDate")));

        Page<postDto> posts = postPage.map(p -> new postDto(p.getId(), p.getTitle(), p.getContent(), p.getMember(),p.getCreatedDate(),p.getLastModifiedDate()));

        model.addAttribute("posts",posts);

        return "posts/posts";
    }

    @GetMapping("/posts/new")
    public String createPost(Model model){
        model.addAttribute("form", new PostCreateForm());

        return "posts/createPostForm";
    }


    @PostMapping("/posts/new")
    public String createPost(@Valid @ModelAttribute("form") PostCreateForm form, BindingResult result, Model model,
                             @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember){
        if (loginMember==null){
            return "redirect:/home";
        }

        if (result.hasErrors()) {
            log.info("errors={}", result);
            return "posts/createPostForm";
        }

        Post post = form.toEntity(form.getTitle(), form.getContent(), loginMember);
        Long postId = postService.createPost(post);
        log.info("post id = " + postId);

        log.info("loginMember = " + loginMember);
        return "redirect:/posts";

    }


}
