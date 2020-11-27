package com.spring.kkaemiGG.repository;

import com.spring.kkaemiGG.dto.MemberDto;
import com.spring.kkaemiGG.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Transactional
public class JpaMemberRepository implements MemberRepository {

    private final EntityManager em;

    @Autowired
    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findByEmail(String email) {

        List<Member> resultList = em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getResultList();

        return resultList.stream().findAny();

    }

    @Override
    public Optional<Member> findByDto(MemberDto memberDto) {

        List<Member> resultList = em.createQuery("select m from Member m where m.email = :email and m.password = :password", Member.class)
                .setParameter("email", memberDto.getEmail())
                .setParameter("password", memberDto.getPassword())
                .getResultList();

        return resultList.stream().findAny();
    }
}
