package com.mytube.Controller.api;

import com.mytube.domain.Follow;
import com.mytube.dto.followDto;
import com.mytube.service.MemberService;
import com.mytube.service.PostService;
import com.mytube.upload.UploadImage;
import com.mytube.web.validator.MemberUpdateFormValidator;
import com.mytube.web.validator.SignUpFormValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberApiController {
    private final SignUpFormValidator signUpFormValidator;
    private final MemberUpdateFormValidator memberUpdateFormValidator;

    private final MemberService memberService;
    private final PostService postService;
    private final UploadImage uploadImage;

    @PostMapping("/members/{fromId}/follow/{toId}")
    public ResponseEntity<Object> follow (@PathVariable Long fromId, @PathVariable Long toId) throws URISyntaxException {

        URI redirectUri = new URI("http://localhost:8080/members/"+fromId+"/info");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUri);

        Long followId = memberService.getFollowId(fromId, toId);
        if (followId!=null){
            log.info("중복 팔로우 요청");
            return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        }
        Follow follow = memberService.follow(fromId, toId);
        followDto followDto = new followDto(follow.getFromMember().getUserId(), follow.getToMember().getUserId());

        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }

    @PostMapping("/members/{fromId}/unfollow/{toId}")
    public ResponseEntity<Object> unfollow (@PathVariable Long fromId, @PathVariable Long toId) throws URISyntaxException {

        URI redirectUri = new URI("http://localhost:8080/members/"+fromId+"/info");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUri);

        Follow follow = memberService.unfollow(fromId, toId);

        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }


}
