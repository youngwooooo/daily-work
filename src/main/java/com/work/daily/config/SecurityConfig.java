package com.work.daily.config;

import com.work.daily.login.service.CustomLoginFailHandler;
import com.work.daily.login.service.CustomLoginSuccessHandler;
import com.work.daily.login.service.CustomOAuth2UserService;
import com.work.daily.login.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // 해당 클래스를 Security 필터로 등록함
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomOAuth2UserService customOauth2UserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // Security 로그인 시, 요청받은 회원 password를 해쉬화하여 DB에 저장된 회원의 password와 비교를 해준다.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    // Security에서 세션 관련 정보를 생성, 삭제 등의 역할을 가진 클래스
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    // Session Clustering 환경(WAS가 하나 이상일 때)에서 로그인 방지 처리를 위함
    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher(){
        return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
    }

    /**
     * Spring Security 설정
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // csrf 토큰 비활성화
        http.csrf().disable();

        // 중복 로그인을 위한 세션 처리
        http.sessionManagement()
                .maximumSessions(1) /* session 최대 허용 개수 */
                .maxSessionsPreventsLogin(false) /* false : 중복 로그인 시 기존 로그인 종료 , true : 중복 로그인 시 에러 발생 */
                .sessionRegistry(sessionRegistry()) /* 모든 세션 정보를 가짐 */
                .expiredUrl("/"); /* 세션 만료시 이동 url */

        http.authorizeRequests()
                    .antMatchers("/user/**", "/boards", "/board/**").hasRole("USER") /* /user/** 의 url은 USER 권한을 가진 사용자만 접근 가능 */
                    .antMatchers("/admin/**").hasRole("ADMIN") /* /admin/** 의 url은 ADMIN 권한을 가진 사용자만 접근 가능 */
                    .anyRequest() /* 이 외의 접근 설정 */
                    .permitAll() /* 모두 접근할 수 있도록 함 */
                .and()
                .logout()/* 로그아웃 설정 */
                    .logoutSuccessUrl("/missions") /* 로그아웃 url */
                    .invalidateHttpSession(true) /* 로그아웃 시 세션 제거 */
                    .deleteCookies("JSESSIONID") /* 로그아웃 시 쿠키 제거 */
                    .clearAuthentication(true) /* 로그아웃 시 권한 제거 */
                .and()
                .formLogin() /* form 로그인 설정 */
                    .loginPage("/login") /* 로그인 페이지 */
                    .usernameParameter("userId") /* username 파라미터 이름 변경 */
                    .passwordParameter("userPw") /* password 파라미터 이름 변경 */
                    .loginProcessingUrl("/login") /* 로그인 성공 시 url */
                    .failureHandler(new CustomLoginFailHandler()) /* 로그인 실패 handler */
                    .successHandler(new CustomLoginSuccessHandler()) /* 로그인 성공 handler */
                .and()
                .oauth2Login() /* oauth2 로그인 설정 */
                    .loginPage("/login") /* 로그인 페이지 */
                    .userInfoEndpoint() /* oauth2 로그인 성공 이후 설정 */
                    .userService(customOauth2UserService); /* oauth2 로그인 service */

    }

}
