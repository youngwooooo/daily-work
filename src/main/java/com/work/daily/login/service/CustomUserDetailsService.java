package com.work.daily.login.service;

import com.work.daily.access.dto.UserDto;
import com.work.daily.domain.entity.User;
import com.work.daily.domain.repository.UserRepository;
import com.work.daily.login.auth.CustomUserDetails;
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("username : " + username);
        Optional<User> findUser = userRepository.findById(username);
        if(!findUser.isPresent()){
            throw new IllegalArgumentException("존재하지 않는 회원입니다. ID : " + username);
        }

        UserDto userDto = UserDto.builder()
                            .id(findUser.get().getId())
                            .password(findUser.get().getPassword())
                            .name(findUser.get().getName())
                            .email(findUser.get().getEmail())
                            .phone(findUser.get().getPhone())
                            .role(findUser.get().getRole())
                            .provider(findUser.get().getProvider())
                            .providerId(findUser.get().getProviderId())
                            .build();


        return new CustomUserDetails(userDto);
    }
}
