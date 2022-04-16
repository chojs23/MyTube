package com.mytube;



import com.mytube.domain.Member;
import com.mytube.service.memberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
@RequiredArgsConstructor
public class InitDb {

    private final memberService memberService;

    @PostConstruct
    public void init(){
        Member member= Member.builder()
                .userId("aaa")
                .password("aa1!")
                .userEmail("aaa@aaa.com")
                .build();
        memberService.join(member);
    }

}
