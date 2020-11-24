package com.spring.kkaemiGG.service;

import com.spring.kkaemiGG.entity.Member;
import com.spring.kkaemiGG.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Long save(Member member) {

        memberRepository.save(member);

        return member.getId();
    }

    public boolean validateDuplicateMember(String email) {
        return !memberRepository.findByEmail(email).isPresent();
    }
}
