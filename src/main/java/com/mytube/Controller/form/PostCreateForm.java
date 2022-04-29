package com.mytube.Controller.form;

import com.mytube.domain.Member;
import com.mytube.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateForm {
    @NotBlank(message = "글제목 필수")
    private String title;

    @NotEmpty(message = "내용 필수")
    private String content;

    private Member member;

    @Builder
    public Post toEntity(String title, String content, Member member) {
        return Post.builder()
                .title(title)
                .content(content)
                .member(member)
                .build();

    }
}
