package com.work.daily.login.service;

import com.work.daily.access.dto.UserDto;
import com.work.daily.domain.UserRole;
import com.work.daily.domain.entity.User;
import com.work.daily.domain.repository.UserRepository;
import com.work.daily.login.auth.CustomUserDetails;
import com.work.daily.login.auth.provider.GoogleUserInfo;
import com.work.daily.login.auth.provider.KakaoUserInfo;
import com.work.daily.login.auth.provider.NaverUserInfo;
import com.work.daily.login.auth.provider.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // google 로그인 시 받은 userRequest를 활용하여 강제 회원가입 처리
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("로그인 진행 중인 서비스 정보 : " + userRequest.getClientRegistration());
        log.info("로그인한 사용자의 정보 : " + super.loadUser(userRequest).getAttributes());

        // 로그인한 사용자의 모든 정보를 가진 OAuth2User 객체
        OAuth2User oAuth2User = super.loadUser(userRequest);
        // 구글, 카카오, 네이버 각각의 OAuth2UserInfo 객체 구현
        OAuth2UserInfo oAuth2UserInfo = null;
        // 구글
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")){
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        // 카카오
        }else if(userRequest.getClientRegistration().getRegistrationId().equals("kakao")){
            oAuth2UserInfo = new KakaoUserInfo(String.valueOf(oAuth2User.getAttributes().get("id")), (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account"));
        // 네이버
        }else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")){
            oAuth2UserInfo = new NaverUserInfo((Map<String, Object>) oAuth2User.getAttributes().get("response"));
        }

        /*
           OAuth 로그인 회원가입
           - id는 provideId와 같다.(providerId는 각 API 별로 고유한 값임)
           - 비밀번호를 따로 받지 않기 때문에 uuid로 랜덤 문자를 생성하여 해쉬 암호화 한다.
           - 나머지는 oAuth2UserInfo 객체의 구현 메서드를 활용한다.
         */
        String password = bCryptPasswordEncoder.encode(UUID.randomUUID().toString());
        String name = oAuth2UserInfo.getName();
        String email = oAuth2UserInfo.getEmail();
        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();

        // 회원 존재 여부 확인
        Optional<User> findUser = userRepository.findById(providerId);
        // 존재하지 않을 때 회원가입 처리
        if(!findUser.isPresent()){
            UserDto userDto = UserDto.builder()
                    .id(providerId)
                    .password(password)
                    .name(name)
                    .email(email)
                    .role(UserRole.USER)
                    .provider(provider)
                    .providerId(providerId)
                    .build();

            userRepository.save(userDto.toEntity());
            return new CustomUserDetails(userDto, oAuth2User.getAttributes());
        }

        // 존재한다면 User 객체를 UserDto로 변환하여 리턴
        UserDto toUserDto =  UserDto.builder()
                .id(findUser.get().getId())
                .password(findUser.get().getPassword())
                .name(findUser.get().getName())
                .email(findUser.get().getEmail())
                .role(findUser.get().getRole())
                .provider(findUser.get().getProvider())
                .providerId(findUser.get().getProviderId())
                .build();

        return new CustomUserDetails(toUserDto, oAuth2User.getAttributes());
    }
}