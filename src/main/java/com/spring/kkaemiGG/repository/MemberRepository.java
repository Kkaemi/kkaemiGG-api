package com.spring.kkaemiGG.repository;

import com.spring.kkaemiGG.dto.MemberDto;
import com.spring.kkaemiGG.entity.Member;

import java.util.Optional;

public interface MemberRepository {
    public Member save(Member member);
    public Optional<Member> findById(Long id);
    public Optional<Member> findByEmail(String email);
    public Optional<Member> findByDto(MemberDto memberDto);
}
