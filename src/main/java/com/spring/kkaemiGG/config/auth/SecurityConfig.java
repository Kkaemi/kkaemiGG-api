package com.spring.kkaemiGG.config.auth;

import com.spring.kkaemiGG.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final UserRepository userRepository;

    @Bean
    protected CustomUsernamePasswordAuthenticationFilter getAuthenticationFilter() throws Exception {

        CustomUsernamePasswordAuthenticationFilter authenticationFilter = new CustomUsernamePasswordAuthenticationFilter();

        authenticationFilter.setFilterProcessesUrl("/auth/login");
        authenticationFilter.setAuthenticationManager(this.authenticationManagerBean());
        authenticationFilter.setUsernameParameter("email");
        authenticationFilter.setPasswordParameter("password");
        authenticationFilter.setAuthenticationSuccessHandler(new SavedRequestAwareAuthenticationSuccessHandler());
        authenticationFilter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler());

        return authenticationFilter;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                    .exceptionHandling().authenticationEntryPoint(new BasicAuthenticationEntryPoint())
                .and()
                    .headers().frameOptions().disable()
                .and()
                    .authorizeRequests()
//                    .antMatchers("/community/write").hasRole(Role.USER.name())
                    .antMatchers("/auth/login").permitAll()
                .and()
                    .formLogin().disable()
                        .addFilterAt(getAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                    .logout()
                        .logoutSuccessUrl("/")
                .and()
                    .oauth2Login()
                        .userInfoEndpoint()
                            .userService(customOAuth2UserService);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(
                        email -> userRepository.findByEmail(email)
                        .orElseThrow(() ->  new UsernameNotFoundException("해당 유저가 존재하지 않습니다."))
                )
                .passwordEncoder(encoder());
    }
}
