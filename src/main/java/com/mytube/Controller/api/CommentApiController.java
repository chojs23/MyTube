package com.mytube.Controller.api;


import com.mytube.domain.Member;
import com.mytube.dto.commentDto;
import com.mytube.dto.memberDto;
import com.mytube.service.CommentService;
import com.mytube.web.SessionConst;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CommentApiController {
    private final CommentService commentService;

    @PostMapping("/posts/{id}/comments")
    public ResponseEntity addComment(@PathVariable Long id, @RequestBody commentDto dto,
                                                 @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) memberDto loginMember) {
        return ResponseEntity.ok(commentService.addComment(loginMember.getUserId(), id, dto));
    }
}
