package com.spring.kkaemiGG.service;

import com.spring.kkaemiGG.domain.Member;
import com.spring.kkaemiGG.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class MemberServiceIntegrationTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {

        // Given
        Member member = Member.builder()
                .name("홍길동")
                .email("aaa@aaa.com")
                .password("password").build();

        // When
        Long saveId = memberService.save(member);

        // Then
        Member findMember = memberRepository.findById(saveId).get();
        assertEquals(member.getName(), findMember.getName());
    }

    @Test
    public void 중복_회원_예외() throws Exception {

        //Given
        Member member1 = Member.builder()
                .name("홍길동")
                .email("aaa@aaa.com")
                .password("password1").build();

        Member member2 = Member.builder()
                .name("임꺽정")
                .email("aaa@aaa.com")
                .password("password2").build();

        //When
        memberService.save(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.save(member2));//예외가 발생해야 한다.
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }
}
