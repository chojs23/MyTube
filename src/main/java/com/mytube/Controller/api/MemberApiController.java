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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final SignUpFormValidator signUpFormValidator;
    private final MemberUpdateFormValidator memberUpdateFormValidator;

    private final MemberService memberService;
    private final PostService postService;
    private final UploadImage uploadImage;

    @PostMapping("/api/members/{fromId}/follow/{toId}")
    public followDto follow(@PathVariable Long fromId, @PathVariable Long toId){
        Follow follow = memberService.follow(fromId, toId);
        followDto followDto = new followDto(follow.getFromMember().getUserId(), follow.getToMember().getUserId());

        return followDto;
    }
}
