package com.mytube.Controller.form;

import com.mytube.domain.MemberImage;
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

    private String memberImageName;

    public MemberForm(String userId,String userEmail,List<Post> posts){
        this.userId=userId;
        this.userEmail = userEmail;
        this.posts = posts;
    }
}
