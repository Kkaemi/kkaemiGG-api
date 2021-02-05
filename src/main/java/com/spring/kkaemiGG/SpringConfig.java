package com.spring.kkaemiGG;

import com.spring.kkaemiGG.repository.JpaMemberRepository;
import com.spring.kkaemiGG.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;

@RequiredArgsConstructor
@Configuration
public class SpringConfig {

    private final EntityManager em;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public MemberRepository memberRepository() {
        return new JpaMemberRepository(em);
    }

}
