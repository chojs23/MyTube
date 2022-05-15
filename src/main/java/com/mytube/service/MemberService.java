package com.mytube.service;


import com.mytube.Controller.form.MemberForm;
import com.mytube.Controller.form.MemberJoinForm;
import com.mytube.Controller.form.MemberUpdateForm;
import com.mytube.Utils.CommonUtils;
import com.mytube.domain.Follow;
import com.mytube.domain.Member;
import com.mytube.domain.MemberImage;
import com.mytube.exception.MemberNotFoundException;
import com.mytube.repository.FollowRepository;
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
    private final FollowRepository followRepository;

    @Transactional
    public Long join(Member member){
        log.info("Join member = "+member);
        memberRepository.save(member);
        return member.getId();
    }

    @Transactional
    public void withdrawal(Member member){
        log.info("withdrawal member = "+member);
        memberRepository.delete(member);
    }

    public Member login(String loginId,String password){
        Member findMember = memberRepository.findMemberByUserId(loginId)
                .orElseThrow(()->new MemberNotFoundException("ex"));

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

        MemberImage formMemberImage = form.getMemberImage();
        if (formMemberImage==null){
            formMemberImage = new MemberImage(member, null, null, null);
        }
        MemberImage memberImageByMember_id = memberImageRepository.findMemberImageByMember_Id(id).orElse(formMemberImage);
        memberImageByMember_id.updateMemberImage(formMemberImage.getOrigFileName(),formMemberImage.getSavedName(),formMemberImage.getFilePath());
        log.info("memberImageByMember = " + memberImageByMember_id);
        CommonUtils.saveIfNullId(memberImageByMember_id.getId(),memberImageRepository,memberImageByMember_id);

        member.updateMember(form.getUserId(),form.getNewPassword(),form.getUserEmail(),memberImageByMember_id);
        Member save = memberRepository.save(member);
        log.info("save = " + save);
        return true;
    }

    @Transactional
    public Follow follow(Long fromId, Long toId) {
        Member fromMember = memberRepository.findById(fromId)
                .orElseThrow(()->new MemberNotFoundException("ex"));
        Member toMember = memberRepository.findById(toId)
                .orElseThrow(()->new MemberNotFoundException("ex"));
        Follow save = followRepository.save(Follow.builder()
                .fromMember(fromMember)
                .toMember(toMember)
                .build());

        return save;
    }

    public Long getFollowId(Long fromId,Long toId){
        Member fromMember = memberRepository.findById(fromId)
                .orElseThrow(()->new MemberNotFoundException("ex"));
        Member toMember = memberRepository.findById(toId)
                .orElseThrow(()->new MemberNotFoundException("ex"));

        Follow follow = followRepository.findFollowByFromMemberAndToMember(fromMember, toMember);

        if (follow != null) {
            return follow.getId();
        }
        else{
            return null;
        }
    }


    public void checkUserIdDuplication(String userId) {
        boolean userIdDuplicate = memberRepository.existsByUserId(userId);
        if(userIdDuplicate){
            throw new IllegalStateException("이미 존재하는 아이디 입니다.");
        }
    }
    public void checkUserEmailDuplication(String email) {
        boolean userEmailDuplicate = memberRepository.existsByUserEmail(email);
        if(userEmailDuplicate){
            throw new IllegalStateException("이미 존재하는 이메일 입니다.");
        }
    }




}
