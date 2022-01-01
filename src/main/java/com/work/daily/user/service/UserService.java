package com.work.daily.user.service;

import com.work.daily.access.ReturnResult;
import com.work.daily.domain.entity.User;
import com.work.daily.domain.repository.UserRepository;
import com.work.daily.user.dto.ModifyPasswordDto;
import com.work.daily.user.dto.ModifyUserInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    /**
     * 계정관리 - 회원 정보(이름, 이메일) 변경
     * @param modifyUserInfoDto
     * @return
     */
    @Transactional
    public String modifyUserInfo(ModifyUserInfoDto modifyUserInfoDto){
        Optional<User> findUser = userRepository.findById(modifyUserInfoDto.getId());
        if(!findUser.isPresent()){
            return ReturnResult.ERROR.getValue();
        }

        findUser.get().modifyUserInfo(modifyUserInfoDto.getName(), modifyUserInfoDto.getEmail());

        return ReturnResult.SUCCESS.getValue();
    }

    /**
     * 계정관리 - 비밀번호 변경 - 비밀번호 확인
     * @param modifyPasswordDto
     * @return
     */
    @Transactional(readOnly = true)
    public String validatingPassword(ModifyPasswordDto modifyPasswordDto){
        Optional<User> findUser = userRepository.findById(modifyPasswordDto.getId());
        if(!findUser.isPresent()){
            return ReturnResult.ERROR.getValue();
        }

        // 비밀번호 일치 시
        if(bCryptPasswordEncoder.matches(modifyPasswordDto.getPassword(), findUser.get().getPassword())){
            return ReturnResult.SUCCESS.getValue();
        }

        return ReturnResult.FAIL.getValue();
    }

    /**
     * 계정관리 - 비밀번호 변경 - 비밀번호 확인 - 비밀번호 변경
     * @param modifyPasswordDto
     * @return
     */
    @Transactional
    public String modifyPassword(ModifyPasswordDto modifyPasswordDto){
        // 비밀번호 암호화
        String hashPassword = bCryptPasswordEncoder.encode(modifyPasswordDto.getPassword());

        Optional<User> findUser = userRepository.findById(modifyPasswordDto.getId());
        if(!findUser.isPresent()){
            return ReturnResult.ERROR.getValue();
        }
        // 비밀번호 변경
        findUser.get().modifyPassword(hashPassword);

        log.info("수정 시간 : " + findUser.get().getModifiedDate());

        return ReturnResult.SUCCESS.getValue();
    }


}
