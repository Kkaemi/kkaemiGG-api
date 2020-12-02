package com.spring.kkaemiGG.service;

import com.spring.kkaemiGG.config.auth.dto.SessionMember;
import com.spring.kkaemiGG.dto.MemberDto;
import com.spring.kkaemiGG.entity.Member;
import com.spring.kkaemiGG.entity.Role;
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
import java.util.ArrayList;
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
        return !memberRepository.findByEmail(email).isPresent();
    }

    public boolean validateMember(MemberDto memberDto) {

        String password = memberDto.getPassword();

        memberDto.setPassword(passwordEncoder.encode(password));

        return !memberRepository.findByDto(memberDto).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (!memberRepository.findByEmail(username).isPresent()) {
            throw new UsernameNotFoundException("ID가 존재하지 않거나 비밀번호가 일치하지 않습니다. 다시 시도해주세요.");
        }

        Member member = memberRepository.findByEmail(username).get();

        httpSession.setAttribute("member", new SessionMember(member));

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(member.getRoleKey()));

        return new User(member.getEmail(), member.getPassword(), authorities);
    }
}
