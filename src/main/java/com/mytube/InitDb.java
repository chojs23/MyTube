package com.mytube;



import com.mytube.domain.Member;
import com.mytube.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
@RequiredArgsConstructor
public class InitDb {

    private final MemberService memberService;

    @PostConstruct
    public void init(){
        Member member1= Member.builder()
                .userId("aaa")
                .password("aaa")
                .userEmail("aaa@aaa.com")
                .build();
        Member member2= Member.builder()
                .userId("bbb")
                .password("bbb")
                .userEmail("bbb@bbb.com")
                .build();
        makeMem("a","a","a");
        makeMem("b","a","b");
        makeMem("c","a","c");
        makeMem("d","a","d");
        makeMem("e","a","e");
        makeMem("f","a","f");
        makeMem("g","a","g");
        makeMem("h","a","h");
        makeMem("i","a","i");
        makeMem("j","a","j");
        makeMem("k","a","k");
        memberService.join(member1);
        memberService.join(member2);
    }
    public void makeMem(String userId,String pw,String email){
        Member build = Member.builder()
                .userId(userId)
                .password(pw)
                .userEmail(email)
                .build();
        memberService.join(build);

    }

}
