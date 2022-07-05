package com.mytube.Controller;


import com.mytube.Controller.form.MemberJoinForm;
import com.mytube.Controller.form.MemberUpdateForm;
import com.mytube.Controller.form.PostCreateForm;
import com.mytube.domain.Member;
import com.mytube.domain.Post;
import com.mytube.dto.memberDto;
import com.mytube.dto.postDto;
import com.mytube.repository.PostRepository;
import com.mytube.service.MemberService;
import com.mytube.service.PostService;
import com.mytube.web.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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

    private final MemberService memberService;
    private final ModelMapper modelMapper;

    @GetMapping("/posts")
    public String posts(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) memberDto loginMember, Model model,
                        @RequestParam Optional<Integer> page, @RequestParam Optional<String> sortBy
                        ){
        model.addAttribute("loginMember", loginMember);


        Page<Post> postPage = postService.getPostPage(PageRequest.of(
                page.orElse(0),
                10,
                Sort.Direction.DESC,sortBy.orElse("createdDate")));

        Page<postDto> postsDto = postPage.map(postDto::createPostDtoWithoutComments);
        log.info("posts = " + postsDto);
        model.addAttribute("posts",postsDto);

        return "posts/posts";
    }

    @GetMapping("/posts/search")
    public String search(String keyword, Model model,
                         @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) memberDto loginMember,
                         @RequestParam Optional<Integer> page,@RequestParam Optional<String> sortBy) {
        Page<Post> searchList = postService.search(keyword, PageRequest.of(
                page.orElse(0),
                10,
                Sort.Direction.DESC,sortBy.orElse("createdDate")));
        Page<postDto> searchListDto = searchList.map(postDto::createPostDtoWithoutComments);
        model.addAttribute("searchList", searchListDto);
        model.addAttribute("loginMember", loginMember);
        model.addAttribute("keyword", keyword);
        return "posts/posts-search";
    }

    @GetMapping("/posts/subscription")
    public String followPosts(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) memberDto loginMember, Model model,
                        @RequestParam Optional<Integer> page,@RequestParam Optional<String> sortBy
    ){
        model.addAttribute("loginMember", loginMember);


        Page<Post> postPage = postService.getFollowingPosts(loginMember.getId(), PageRequest.of(
                page.orElse(0),
                10,
                Sort.Direction.DESC,sortBy.orElse("createdDate")));

        Page<postDto> postsDto = postPage.map(postDto::createPostDtoWithoutComments);

        log.info("posts = " + postsDto);
        model.addAttribute("posts",postsDto);

        return "posts/posts";
    }

    @GetMapping("/posts/new")
    public String createPost(Model model){
        model.addAttribute("form", new PostCreateForm());

        return "posts/createPostForm";
    }

    //TODO 세션에 엔티티 보관 X 엔티티를 세션에 보관하면 안됩니다. 세션에는 DTO를 보관해주세요. 지금 세션에 member 엔티티를 보관하고,
    // 또 해당 엔티티를 엮어서 그대로 Post와 함께 persist하기 때문에 post를 다시 조회할 때 세션에 보관된 member가 조회되는 현상이 발생합니다.
    // 이 member는 프록시 기능을 상실한 상태이기 때문에 이런 문제가 발생합니다.
    @PostMapping("/posts/new")
    public String createPost(@Valid @ModelAttribute("form") PostCreateForm form, BindingResult result, Model model,
                             @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) memberDto loginMember){
        if (loginMember==null){
            return "redirect:/home";
        }

        if (result.hasErrors()) {
            log.info("errors={}", result);
            return "posts/createPostForm";
        }

        Member member = memberService.findMember(loginMember.getId());

        Post post = Post.createPost(form.getTitle(), form.getContent(), member);


        Long postId = postService.createPost(post);
        log.info("member's comment = "+member.getComments().size());
        log.info("member's posts = "+member.getPosts().size());
        return "redirect:/posts";

    }

    @GetMapping("/posts/{id}/detail")
    public String postDetail(@PathVariable Long id, Model model,
                             @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) memberDto loginMember) {
        Post post = postService.getPost(id);



        postDto postDto = new postDto(post);

        model.addAttribute("postDto", postDto);
        model.addAttribute("loginMember", loginMember);

        return "/posts/postDetail";

    }
    @GetMapping("/posts/{id}/update")
    public String updatePostForm(@PathVariable Long id, Model model,
                             @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) memberDto loginMember){
        Post post = postService.getPost(id);


        if (loginMember==null || !loginMember.getId().equals(post.getMember().getId())){
            return "redirect:/posts/{id}/detail";
        }



        PostCreateForm postCreateForm = new PostCreateForm(post.getTitle(), post.getContent(), post.getMember());
        model.addAttribute("form", postCreateForm);
        model.addAttribute("loginMember", loginMember);


        return "/posts/updatePostForm";
    }

    @PostMapping("/posts/{id}/update")
    public String updatePost(@PathVariable Long id,@Valid @ModelAttribute("form") PostCreateForm form,BindingResult result,
                             @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) memberDto loginMember,
                             RedirectAttributes redirectAttributes) {
        Post post = postService.getPost(id);
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
    public String deletePost(@PathVariable Long id,@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) memberDto loginMember,
                             RedirectAttributes redirectAttributes){
        Post post = postService.getPost(id);
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
