package com.spring.kkaemiGG.repository;

import com.spring.kkaemiGG.web.dto.MemberDto;
import com.spring.kkaemiGG.entity.Member;

import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(Long id);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByDto(MemberDto memberDto);
}
