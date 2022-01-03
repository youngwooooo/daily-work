package com.work.daily.user.service;

import com.work.daily.access.ReturnResult;
import com.work.daily.domain.entity.User;
import com.work.daily.domain.repository.UserRepository;
import com.work.daily.login.auth.CustomUserDetails;
import com.work.daily.login.dto.LoginUserDto;
import com.work.daily.user.dto.ModifyPasswordDto;
import com.work.daily.user.dto.ModifyUserInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public UserDetails modifyUserInfo(ModifyUserInfoDto modifyUserInfoDto){
        Optional<User> findUser = userRepository.findByUserId(modifyUserInfoDto.getUserId());
        if(!findUser.isPresent()){
            throw new UsernameNotFoundException("회원정보가 존재하지 않습니다. ID : " + modifyUserInfoDto.getUserId());
        }

        findUser.get().modifyUserInfo(modifyUserInfoDto.getUserNm(), modifyUserInfoDto.getUserEmail());

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

        UserDetails modifyUserDetails = new CustomUserDetails(loginUserDto);


        return modifyUserDetails;
    }

    /**
     * 계정관리 - 비밀번호 변경 - 비밀번호 확인
     * @param modifyPasswordDto
     * @return
     */
    @Transactional(readOnly = true)
    public String validatingPassword(ModifyPasswordDto modifyPasswordDto){
        Optional<User> findUser = userRepository.findByUserId(modifyPasswordDto.getUserId());
        if(!findUser.isPresent()){
            return ReturnResult.ERROR.getValue();
        }

        // 비밀번호 일치 시
        if(bCryptPasswordEncoder.matches(modifyPasswordDto.getUserPw(), findUser.get().getUserPw())){
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
        String hashPassword = bCryptPasswordEncoder.encode(modifyPasswordDto.getUserPw());

        Optional<User> findUser = userRepository.findByUserId(modifyPasswordDto.getUserId());
        if(!findUser.isPresent()){
            return ReturnResult.ERROR.getValue();
        }
        // 비밀번호 변경
        findUser.get().modifyPassword(hashPassword);

        log.info("수정 시간 : " + findUser.get().getModifiedDate());

        return ReturnResult.SUCCESS.getValue();
    }


}
