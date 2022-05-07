package com.mytube.Controller.form;


import com.mytube.domain.Member;
import com.mytube.domain.VideoFile;
import com.mytube.web.validator.MultipartFileSizeValid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoCreateForm {
    @NotBlank(message="제목 필수")
    private String title;

    @MultipartFileSizeValid
    private MultipartFile attachFile;

    private Member member;
}
