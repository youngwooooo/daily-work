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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    @Value("${custom.path.profile-image}")
    private String profileUploadPath;

    @Value("${custom.path.root-image}")
    private String rootImagePath;

    /**
     * 계정관리 - 회원 정보(이름, 이메일, 프로필 사진) 변경
     * @param modifyUserInfoDto
     * @return
     */
    @Transactional
    public UserDetails modifyUserInfo(ModifyUserInfoDto modifyUserInfoDto, MultipartFile file) throws IOException {

        Optional<User> findUser = userRepository.findByUserId(modifyUserInfoDto.getUserId());
        if(!findUser.isPresent()){
            throw new UsernameNotFoundException("회원정보가 존재하지 않습니다. ID : " + modifyUserInfoDto.getUserId());
        }

        // file이 존재할 때
        if(file != null){
            /*
                uploadFolder : 서버에 업로드될 폴더
                uploadFolder가 존재하지 않으면 생성
             */
            File uploadFolder = new File(profileUploadPath + modifyUserInfoDto.getUserId());
            if(!uploadFolder.exists()){
                uploadFolder.mkdir();
            }

            /*
                beforeProfileImage : 현재 회원의 프로필 사진
                프로필 사진 변경 시, 변경 전 프로필 사진 삭제
             */
            File beforeProfileImage = new File(rootImagePath + findUser.get().getProfileImage());
            if(beforeProfileImage.exists()){
                beforeProfileImage.delete();
            }

            /*
                fileOriginalName : 프로필 사진명
                파일명이 한글일 경우를 위해 UTF-8로 encode
             */
            String fileOriginalName = URLEncoder.encode(file.getOriginalFilename(), "UTF-8");

            // profileImage : 새로운 프로필 사진
            File profileImage = new File(uploadFolder, fileOriginalName);
            // 서버에 해당 프로필 사진(파일) 생성
            file.transferTo(profileImage);

            // profileImagePath : 화면 img src에 넣을 프로필 사진 경로
            String profileImagePath = "/profile/" + modifyUserInfoDto.getUserId() + "/" + fileOriginalName;

            // 프로필 사진 변경
            findUser.get().modifyUserInfo(modifyUserInfoDto.getUserNm(), modifyUserInfoDto.getUserEmail(), profileImagePath);
        }

        // 프로필 사진을 변경하지 않았을 때, 기존 이미지로 유지
        findUser.get().modifyUserInfo(modifyUserInfoDto.getUserNm(), modifyUserInfoDto.getUserEmail(), findUser.get().getProfileImage());

        // userDetails 객체를 만들기 위한 LoginUserDto 객체 생성
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

        // 세션 변경 위한 새로운 userDetails 객체 생성
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

        log.info("수정 시간 : " + findUser.get().getUpdDtm());

        return ReturnResult.SUCCESS.getValue();
    }


}
