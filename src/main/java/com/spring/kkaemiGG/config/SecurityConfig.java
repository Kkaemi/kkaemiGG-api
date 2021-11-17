package com.spring.kkaemiGG.config;

import com.spring.kkaemiGG.auth.CustomOAuth2UserService;
import com.spring.kkaemiGG.domain.user.Role;
import com.spring.kkaemiGG.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final UserRepository userRepository;

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()

                .headers().frameOptions().disable()

                .and()

                .authorizeRequests()
                .antMatchers("/", "/css/**", "/img/**", "/js/**", "/h2-console/**", "/profile").permitAll()
                .antMatchers("/community/write", "/community/write/").hasRole(Role.USER.name())

                .and()

                .formLogin().disable()
                .logout()
                .logoutSuccessUrl("/")

                .and()

                .oauth2Login()
                .userInfoEndpoint()
                .userService(customOAuth2UserService);
    }
}
