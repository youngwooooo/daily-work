package com.work.daily.login.service;

import com.work.daily.domain.entity.User;
import com.work.daily.domain.repository.UserRepository;
import com.work.daily.login.auth.CustomUserDetails;
import com.work.daily.login.dto.LoginUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // 로그인 시, Security가 해당 Service로 들어오고 해당 함수를 실행시켜 로그인을 해준다.
    // UserDetails 객체를 리턴하여 Authentication 객체에 넣어주고 Session을 만들어준다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername 호출 - ID : " + username);

        Optional<User> findUser = userRepository.findByUserId(username);
        if(!findUser.isPresent()){
            throw new UsernameNotFoundException("존재하지 않는 회원입니다.");
        }

        LoginUserDto loginUserDto = LoginUserDto.builder()
                            .userSeq(findUser.get().getUserSeq())
                            .userId(findUser.get().getUserId())
                            .userPw(findUser.get().getUserPw())
                            .userNm(findUser.get().getUserNm())
                            .userEmail(findUser.get().getUserEmail())
                            .role(findUser.get().getRole())
                            .provider(findUser.get().getProvider())
                            .profileImage(findUser.get().getProfileImage())
                            .build();

        log.info(loginUserDto.toString());

        return new CustomUserDetails(loginUserDto);
    }
}
