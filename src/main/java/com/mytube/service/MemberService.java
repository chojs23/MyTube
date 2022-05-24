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
        memberImageRepository.save(new MemberImage(member, null, null, null));
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
        return memberRepository.findAllMemberPage(pageable);
    }


    public Member findMember(Long id){
        return memberRepository.findById(id).orElseThrow(()->new MemberNotFoundException("ex"));
    }

    @Transactional
    public boolean updateMember(Long id, MemberUpdateForm form){
        Member member = memberRepository.findById(id).orElseThrow(()->new MemberNotFoundException("ex"));


        log.info("member = " + member);

        MemberImage formMemberImage = form.getMemberImage();
        if (formMemberImage!=null){
            //MemberImage memberImageByMember_id = memberImageRepository.findMemberImageByMember_Id(member.getId()).get();
            MemberImage memberImageByMember_id = member.getMemberImage();
            memberImageByMember_id.updateMemberImage(formMemberImage.getOrigFileName(),formMemberImage.getSavedName(),formMemberImage.getFilePath());
            //member.updateMember(form.getUserId(),form.getNewPassword(),form.getUserEmail(),formMemberImage);
        }
        else{
            member.updateMember(form.getUserId(),form.getNewPassword(),form.getUserEmail());
        }
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

    @Transactional
    public Follow unfollow(Long fromId, Long toId) {
        Member fromMember = memberRepository.findById(fromId)
                .orElseThrow(()->new MemberNotFoundException("ex"));
        Member toMember = memberRepository.findById(toId)
                .orElseThrow(()->new MemberNotFoundException("ex"));
        Follow del_follow = followRepository.findFollowByFromMemberAndToMember(fromMember, toMember);
        followRepository.delete(del_follow);

        return del_follow;
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






}
