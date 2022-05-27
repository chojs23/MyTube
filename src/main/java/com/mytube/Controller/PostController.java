package com.mytube.Controller;


import com.mytube.Controller.form.MemberJoinForm;
import com.mytube.Controller.form.MemberUpdateForm;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
                        @RequestParam Optional<Integer> page,@RequestParam Optional<String> sortBy
                        ){
        model.addAttribute("loginMember", loginMember);


        Page<Post> postPage = postService.getPostPage(PageRequest.of(
                page.orElse(0),
                10,
                Sort.Direction.DESC,sortBy.orElse("createdDate")));

        Page<postDto> postsDto = postPage.map(p -> new postDto(p));
        log.info("posts = " + postsDto);
        model.addAttribute("posts",postsDto);

        return "posts/posts";
    }

    @GetMapping("/posts/search")
    public String search(String keyword, Model model,
                         @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                         @RequestParam Optional<Integer> page,@RequestParam Optional<String> sortBy) {
        Page<Post> searchList = postService.search(keyword, PageRequest.of(
                page.orElse(0),
                10,
                Sort.Direction.DESC,sortBy.orElse("createdDate")));
        Page<postDto> searchListDto = searchList.map(p -> new postDto(p));
        model.addAttribute("searchList", searchListDto);
        model.addAttribute("loginMember", loginMember);
        model.addAttribute("keyword", keyword);
        return "posts/posts-search";
    }

    @GetMapping("/posts/subscription")
    public String followPosts(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model,
                        @RequestParam Optional<Integer> page,@RequestParam Optional<String> sortBy
    ){
        model.addAttribute("loginMember", loginMember);


        Page<Post> postPage = postService.getFollowingPosts(loginMember.getId(), PageRequest.of(
                page.orElse(0),
                10,
                Sort.Direction.DESC,sortBy.orElse("createdDate")));

        Page<postDto> postsDto = postPage.map(p -> new postDto(p));
        log.info("posts = " + postsDto);
        model.addAttribute("posts",postsDto);

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

    @GetMapping("/posts/{id}/detail")
    public String postDetail(@PathVariable Long id, Model model,
                             @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) {
        Optional<Post> post = postService.getPost(id);

        if (post.isEmpty()){
            log.info("PostDetail => Empty post");
            return "redirect:/posts";
        }
        Post findPost = post.get();
        postDto postDto = new postDto(findPost);

        model.addAttribute("postDto", postDto);
        model.addAttribute("loginMember", loginMember);

        return "/posts/postDetail";

    }
    @GetMapping("/posts/{id}/update")
    public String UpdatePostForm(@PathVariable Long id, Model model,
                             @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember){
        Optional<Post> post = postService.getPost(id);
        if (post.isEmpty()){
            log.info("PostDetail => Empty post");
            return "redirect:/posts";
        }

        if (loginMember==null || !loginMember.getId().equals(post.get().getMember().getId())){
            return "redirect:/posts/{id}/detail";
        }


        Post findPost = post.get();
        PostCreateForm postCreateForm = new PostCreateForm(findPost.getTitle(), findPost.getContent(), findPost.getMember());
        model.addAttribute("form", postCreateForm);
        model.addAttribute("loginMember", loginMember);


        return "/posts/updatePostForm";
    }

    @PostMapping("/posts/{id}/update")
    public String UpdatePost(@PathVariable Long id,@Valid @ModelAttribute("form") PostCreateForm form,BindingResult result,
                             @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                             RedirectAttributes redirectAttributes) {
        Post post = postService.getPost(id).get();
        Member member = post.getMember();
        if (!loginMember.getId().equals(member.getId())){
            log.info("잘못된 사용자 게시글 수정");
            redirectAttributes.addAttribute("id", id);
            return "redirect:/posts/{id}/detail";
        }

        if (result.hasErrors()) {
            log.info("errors={}", result);
            return "posts/updatePostForm";
        }


        postService.updatePost(id, form);

        return "redirect:/posts/{id}/detail";
    }

    @GetMapping("/posts/{id}/delete")
    public String deletePost(@PathVariable Long id,@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                             RedirectAttributes redirectAttributes){
        Post post = postService.getPost(id).get();
        Member member = post.getMember();
        if (!loginMember.getId().equals(member.getId())){
            log.info("잘못된 사용자 게시글 삭제");
            redirectAttributes.addAttribute("id", id);
            return "redirect:/posts/{id}/detail";
        }

        Post deletePost = postService.deletePost(id);

        log.info("del post = " + deletePost);
        return "redirect:/posts";
    }
}
