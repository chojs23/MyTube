package com.mytube.service;


import com.mytube.domain.Member;
import com.mytube.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class memberService {
    private final MemberRepository memberRepository;

    @Transactional
    public Long join(Member member){
        log.info("Join member = "+member);
        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }
    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findMemberByUserId(member.getUserId());
        if (!(findMember==null)) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }
}
