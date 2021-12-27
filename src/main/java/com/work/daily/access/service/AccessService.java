package com.work.daily.access.service;

import com.work.daily.access.ReturnResult;
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

    /**
     * 회원가입
     * @param userDto
     * @return SUCCESS, FAIL
     */
    @Transactional
    public String save(UserDto userDto){
        // userDto.getId()로 회원 찾기
        Optional<User> findUser = userRepository.findById(userDto.getId());
        // 회원이 존재할 때
        if(findUser.isPresent()){
            return ReturnResult.FAIL.getValue();
        }

        userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        userDto.setRole(UserRole.USER);

        userRepository.save(userDto.toEntity());
        log.info("save() : 회원가입 성공");

        return ReturnResult.SUCCESS.getValue();
    }

}
