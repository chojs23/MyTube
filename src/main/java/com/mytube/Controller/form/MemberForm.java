package com.mytube.Controller.form;

import com.mytube.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberForm {
    private String userId;

    private String userEmail;

    private List<Post> posts;
}
