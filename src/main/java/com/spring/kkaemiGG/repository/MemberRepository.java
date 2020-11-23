package com.spring.kkaemiGG.repository;

import com.spring.kkaemiGG.bean.MemberDTO;

import java.util.Optional;

public interface MemberRepository {
    public MemberDTO save(MemberDTO memberDTO);
    public Optional<MemberDTO> findByEmail(String email);
}
