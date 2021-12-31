package com.work.daily.access.service;

import com.work.daily.access.ReturnResult;
import com.work.daily.access.dto.JoinUserDto;
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

    /**
     * 회원가입
     * @param joinUserDto
     * @return SUCCESS, FAIL
     */
    @Transactional
    public String save(JoinUserDto joinUserDto){
        log.info("AccessService::save called");

        // userDto.getId()로 회원 찾기
        Optional<User> findUser = userRepository.findById(joinUserDto.getId());

        // 회원이 존재할 때
        if(findUser.isPresent())
            return ReturnResult.FAIL.getValue();

        joinUserDto.setPassword(bCryptPasswordEncoder.encode(joinUserDto.getPassword()));
        joinUserDto.setRole(UserRole.USER);
        joinUserDto.setProfileImage("/img/common/basic_profile.png");

        userRepository.save(joinUserDto.toEntity());

        return ReturnResult.SUCCESS.getValue();
    }

}
