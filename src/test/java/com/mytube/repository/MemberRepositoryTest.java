package com.mytube.repository;

import com.mytube.domain.Member;
import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@Rollback(false)
public class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Test
    public void testMember() {
        Member member = Member.builder()
                .userId("memberA")
                .password("1234")
                .userEmail("example")
                .build();

        Member savedMember = memberRepository.save(member);
        Member findMember =
                memberRepository.findById(savedMember.getId()).get();

        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());

        Assertions.assertThat(findMember.getUserId()).isEqualTo(member.getUserId())
        ;
        Assertions.assertThat(findMember).isEqualTo(member); //JPA 엔티티 동일성

    }
}