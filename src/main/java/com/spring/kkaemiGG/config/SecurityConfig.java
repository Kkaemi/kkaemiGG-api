package com.spring.kkaemiGG.config;

import com.spring.kkaemiGG.auth.CustomOAuth2UserService;
import com.spring.kkaemiGG.auth.JwtAuthenticationFilter;
import com.spring.kkaemiGG.auth.JwtEntryPoint;
import com.spring.kkaemiGG.auth.Oauth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final Oauth2SuccessHandler oauth2SuccessHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtEntryPoint jwtEntryPoint;

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
                .antMatchers(HttpMethod.GET, "/api/v1/posts/**").permitAll()
                .anyRequest().authenticated()

                .and()

                .formLogin().disable()
                .exceptionHandling().authenticationEntryPoint(jwtEntryPoint)

                .and()

                .oauth2Login()
                .successHandler(oauth2SuccessHandler)
                .userInfoEndpoint()
                .userService(customOAuth2UserService);

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
