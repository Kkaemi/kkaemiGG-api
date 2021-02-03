package com.spring.kkaemiGG.service;

import com.spring.kkaemiGG.config.auth.dto.SessionMember;
import com.spring.kkaemiGG.web.dto.MemberDto;
import com.spring.kkaemiGG.entity.Member;
import com.spring.kkaemiGG.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final HttpSession httpSession;

    public Long join(MemberDto memberDto) {

        String password = memberDto.getPassword();

        memberDto.setPassword(passwordEncoder.encode(password));

        Member member = memberDto.toEntity();

        return memberRepository.save(member).getId();
    }

    public boolean validateDuplicateMember(String email) {
        return memberRepository.findByEmail(email).isEmpty();
    }

    public boolean validateMember(MemberDto memberDto) {

        String password = memberDto.getPassword();

        memberDto.setPassword(passwordEncoder.encode(password));

        return memberRepository.findByDto(memberDto).isEmpty();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return memberRepository.findByEmail(username).map(
                // Email 검색한 유저가 있을 경우
                member -> {
                    // Session 추가
                    httpSession.setAttribute("member", new SessionMember(member));

                    // User 객체 생성하고 반환
                    List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(member.getRoleKey()));
                    return new User(member.getEmail(), member.getPassword(), authorities);
                }).orElseThrow(
                        // Email 검색한 유저가 없을 겨우 Exception 발생
                        () -> new UsernameNotFoundException("ID가 존재하지 않거나 비밀번호가 일치하지 않습니다. 다시 시도해주세요.")
        );

    }
}
