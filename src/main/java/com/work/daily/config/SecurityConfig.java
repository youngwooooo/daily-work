package com.work.daily.config;

import com.work.daily.login.service.CustomLoginFailHandler;
import com.work.daily.login.service.CustomLoginSuccessHandler;
import com.work.daily.login.service.CustomOAuth2UserService;
import com.work.daily.login.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // 해당 클래스를 Security 필터로 등록함
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomOAuth2UserService customOauth2UserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    // Security 로그인 시, 요청받은 회원 password를 해쉬화하여 DB에 저장된 회원의 password와 비교를 해준다.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll()
                .and()
                .logout()
                    .logoutSuccessUrl("/")
                .and()
                .formLogin()
                    .loginPage("/login")
                    .usernameParameter("userId")
                    .passwordParameter("userPw")
                    .loginProcessingUrl("/login")
                    .failureHandler(new CustomLoginFailHandler())
                    .successHandler(new CustomLoginSuccessHandler())
                .and()
                .oauth2Login()
                    .loginPage("/login")
                    .userInfoEndpoint()
                    .userService(customOauth2UserService);
    }

}
