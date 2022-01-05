package com.work.daily;

import com.work.daily.login.auth.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@Slf4j
@EnableJpaAuditing
@SpringBootApplication
public class DailyApplication {

    // 해쉬 암호화
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        SpringApplication.run(DailyApplication.class, args);
    }

    // 생성자, 수정자 자동으로 입력
    @Bean
    public AuditorAware<String> auditorAware(){
        return new AuditorAware<String>() {
            @Override
            public Optional<String> getCurrentAuditor() {
                /*
                    - Security를 사용하기 때문에 authentication 객체를 활용하여 입력되게 한다.
                      : SecurityContextHolder.getContext().getAuthentication()으로 Security 세션 정보를 가져올 수 있다.
                        1) AnonymousAuthenticationToken : Security를 활용 시, 서버에 접속한 모든 사용자들이 기본적으로 갖게 되는 세션
                        2) UsernamePasswordAuthenticationToken : 일반 로그인 시 갖게 되는 세션
                        3) OAuth2AuthenticationToken : OAuth2 로그인 시 갖게 되는 세션

                    - 조건 설명
                      1) authentication == null : Authentication 객체가 존재하지 않을 때
                      2) !authentication.isAuthenticated() : 인증된 사용자가 아닐 때
                      3) "anonymousUser".equals(authentication.getPrincipal()) : 익명 사용자를 판단하기 위함

                    - return
                      1) null : User Entity의 @PrePersist을 붙인 setInsAndUpdUser()를 활용하여 생성자와 수정자에 userId 값을 넣어준다.
                      2) Optional.of(((CustomUserDetails) authentication.getPrincipal()).getUsername())
                         : 데이터 수정 시, 로그인한 사용자의 userId 값을 수정자의 값으로 넣는다.
                           생성자는 updatable = false 이므로 바뀌지 않는다.
                 */
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if(authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())){
                   return null;
                }
                return Optional.of(((CustomUserDetails) authentication.getPrincipal()).getUsername());
            }
        };
    }

}
