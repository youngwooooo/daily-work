package com.work.daily.init;

import com.work.daily.access.dto.JoinUserDto;
import com.work.daily.domain.UserRole;
import com.work.daily.domain.entity.User;
import com.work.daily.domain.repository.UserRepository;
import com.work.daily.login.auth.provider.OAuth2UserInfo;
import com.work.daily.login.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Transactional
//@Order
@Configuration
@Component("DbUserInfoInsert")
public class DbUserInfoInsert {
    private final UserRepository userRepository;

    /**
     * @filename : DbUserInfoInsert.java
     * @description : 사용자 등록
     * @author : 이준범
     */
    //@PostConstruct
    @Bean("userInfoInsert")
    public void userInfoInsert() {
        // pw 설정
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String userPw = bCryptPasswordEncoder.encode("1234");

        List<User> saveUserList = new ArrayList<>();

        // 구글
        for (int i = 0; i < 6; i++) {
            JoinUserDto joinUserDto = JoinUserDto.builder()
                    .userId("googleUser"+i)
                    .userPw(userPw)
                    .userNm("구글 유저"+i+"번")
                    .userEmail("구글 유저"+i+"번@google.com")
                    .role(UserRole.USER)
                    .provider("test")
                    .build();
            saveUserList.add(joinUserDto.toEntity());
        }

        // 네이버
        for (int i = 0; i < 6; i++) {
            JoinUserDto joinUserDto = JoinUserDto.builder()
                    .userId("naverUser"+i)
                    .userPw(userPw)
                    .userNm("네이버 유저"+i+"번")
                    .userEmail("네이버 유저"+i+"번@naver.com")
                    .role(UserRole.USER)
                    .provider("test")
                    .build();

            saveUserList.add(joinUserDto.toEntity());
        }

        // 카카오
        for (int i = 0; i < 6; i++) {
            JoinUserDto joinUserDto = JoinUserDto.builder()
                    .userId("kakaoUser"+i)
                    .userPw(userPw)
                    .userNm("카카오 유저"+i+"번")
                    .userEmail("카카오 유저"+i+"번@kakao.com")
                    .role(UserRole.USER)
                    .provider("test")
                    .build();

            saveUserList.add(joinUserDto.toEntity());
        }

        // 관리자
        for (int i = 1; i < 3; i++) {
            JoinUserDto joinUserDto = JoinUserDto.builder()
                    .userId("adminUser"+i)
                    .userPw(userPw)
                    .userNm("관리자"+i+"번")
                    .userEmail("관리자"+i+"번@admin.com")
                    .role(UserRole.ADMIN)
                    .build();

            saveUserList.add(joinUserDto.toEntity());
        }

        userRepository.saveAll(saveUserList);


    }


}
