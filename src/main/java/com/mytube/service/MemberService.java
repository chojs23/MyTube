package com.mytube.service;


import com.mytube.Controller.form.MemberForm;
import com.mytube.Controller.form.MemberJoinForm;
import com.mytube.Controller.form.MemberUpdateForm;
import com.mytube.domain.Member;
import com.mytube.repository.MemberImageRepository;
import com.mytube.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberImageRepository memberImageRepository;


    @Transactional
    public Long join(Member member){
        log.info("Join member = "+member);
        memberRepository.save(member);
        return member.getId();
    }

    public Member login(String loginId,String password){
        Member findMember = memberRepository.findMemberByUserId(loginId);
        if (findMember==null){
            return null;
        }
        return findMember.getPassword().equals(password)?findMember:null;
    }
    /**
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }
     */

    public Page<Member> getMemberPage(Pageable pageable){
        return memberRepository.findAll(pageable);
    }

    public Optional<Member> findMember(Long id){
        return memberRepository.findById(id);
    }

    @Transactional
    public boolean updateMember(Long id, MemberUpdateForm form){
        Optional<Member> findMember = memberRepository.findById(id);

        if(!findMember.isPresent()){
            log.info("isPresent false");
            return false;
        }
        Member member = findMember.get();
        log.info("member = " + member);

        if (!member.getPassword().equals(form.getOldPassword())){
            log.info("oldPassword err");
            return false;
        }

        memberImageRepository.save(form.getMemberImage());
        member.updateMember(form.getUserId(),form.getNewPassword(),form.getUserEmail(),form.getMemberImage());
        Member save = memberRepository.save(member);
        log.info("save = " + save);
        return true;
    }

    public boolean checkUserIdDuplication(String userId) {
        return memberRepository.existsByUserId(userId);
    }
    public boolean checkUserEmailDuplication(String email) {
        return memberRepository.existsByUserEmail(email);
    }




}
