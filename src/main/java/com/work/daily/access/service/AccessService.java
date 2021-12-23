package com.work.daily.access.service;

import com.work.daily.access.dto.UserDto;
import com.work.daily.domain.UserRole;
import com.work.daily.domain.entity.User;
import com.work.daily.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccessService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 회원 가입
    @Transactional
    public void save(UserDto userDto){
        Optional<User> findUser = userRepository.findById(userDto.getId());

        if(!findUser.isPresent()){
            userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
            userDto.setRole(UserRole.USER);

            userRepository.save(userDto.toEntity());
            log.info("save 성공");
        }else {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }

    }


}
