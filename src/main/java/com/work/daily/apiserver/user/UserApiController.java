package com.work.daily.apiserver.user;

import com.work.daily.access.ReturnResult;
import com.work.daily.access.dto.ResponseDto;
import com.work.daily.user.dto.ModifyPasswordDto;
import com.work.daily.user.dto.ModifyUserInfoDto;
import com.work.daily.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;

    /**
     * 계정관리(마이페이지) - 회원 정보(이름, 이메일, 프로필 사진) 변경
     * @RequestPart : multipart/form-data를 받기 위함
     * @param modifyUserInfoDto
     * @param bindingResult
     * @return
     */
    @PatchMapping("/user/mypage")
    public ResponseEntity<ResponseDto> modifyUserInfo(@Valid @RequestPart(value = "modifyUserInfoDto") ModifyUserInfoDto modifyUserInfoDto
                                                        , @RequestPart(value = "file", required = false) MultipartFile file
                                                        , BindingResult bindingResult
                                                        , HttpSession session) throws IOException {

        log.info("UserApiController :: modifyUserInfo called");

        // 유효성 검사 오류 발생 시
        if(bindingResult.hasErrors()){
            log.info("UserApiController :: modifyUserInfo :: 유효성 검사 오류 = " + bindingResult.getFieldErrors());
            Map<String, Object> errorMap = new HashMap<>();
            for(FieldError error : bindingResult.getFieldErrors()){
                errorMap.put(error.getField(), error.getDefaultMessage());
            }

            return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.BAD_REQUEST.value()).data(errorMap).message("입력된 값이 올바르지 않습니다.").build()
                    , HttpStatus.BAD_REQUEST);
        }


        // modifyUserDetails : 회원정보 변경한 UserDetails 객체
        UserDetails modifyUserDetails = userService.modifyUserInfo(modifyUserInfoDto, file);

        // 세션 초기화
        SecurityContextHolder.clearContext();
        // 새로운 Authentication 객체 생성
        Authentication newAuthentication = new UsernamePasswordAuthenticationToken(modifyUserDetails, null, modifyUserDetails.getAuthorities());
        // security 세션에 새로운 Authentication 객체 저장
        SecurityContextHolder.getContext().setAuthentication(newAuthentication);
        // security 세션을 HttpSession에 저장
        session.setAttribute("SPRING_SECURITY_CONTEXT", newAuthentication);

        // SUCCESS
        return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.OK.value()).data(ReturnResult.SUCCESS.getValue()).message("회원 정보 변경이 완료되었습니다.").build(), HttpStatus.OK);
    }

    /**
     * 계정 관리(마이페이지) - 비밀번호 변경 - 비밀번호 확인
     * @param modifyPasswordDto
     * @param bindingResult
     * @return
     */
    @PostMapping("/user/mypage/valid/password")
    public ResponseEntity<ResponseDto> validatingPassword(@RequestBody @Valid ModifyPasswordDto modifyPasswordDto, BindingResult bindingResult){
        log.info("UserApiController :: passwordModifyForm called");

        // 유효성 검사 오류 발생 시
        if(bindingResult.hasErrors()){
            log.info("UserApiController :: passwordModifyForm :: 유효성 검사 오류 = " + bindingResult.getFieldErrors());
            Map<String, Object> errorMap = new HashMap<>();
            for(FieldError error : bindingResult.getFieldErrors()){
                errorMap.put(error.getField(), error.getDefaultMessage());
            }

            return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.BAD_REQUEST.value()).data(errorMap).message("비밀번호가 일치하지 않습니다.").build()
                                        , HttpStatus.BAD_REQUEST);
        }

        // result = SUCCESS / FAIL / ERROR
        String result = userService.validatingPassword(modifyPasswordDto);

        // FAIL
        if(result.equals(ReturnResult.FAIL.getValue())){
            return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.BAD_REQUEST.value()).data(ReturnResult.FAIL.getValue()).message("비밀번호가 일치하지 않습니다.").build()
                    , HttpStatus.BAD_REQUEST);
        }else if(result.equals(ReturnResult.ERROR.getValue())){
            throw new UsernameNotFoundException("UserApiController :: passwordModifyForm :: 존재하는 회원이 아닙니다. ID = " + modifyPasswordDto.getUserId());
        }

        // SUCCESS
        return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.OK.value()).data(ReturnResult.SUCCESS.getValue()).message("비밀번호가 일치합니다.").build(), HttpStatus.OK);

    }

    /**
     *  계정관리(마이페이지) - 비밀번호 변경 - 비밀번호 확인 - 비밀번호 변경
     * @param modifyPasswordDto
     * @param bindingResult
     * @return
     */
    @PatchMapping("/user/mypage/password")
    public ResponseEntity<ResponseDto> modifyPassword(@RequestBody @Valid ModifyPasswordDto modifyPasswordDto, BindingResult bindingResult){
        log.info("UserApiController :: modifyPassword called");

        // 유효성 검사 오류 발생 시
        if(bindingResult.hasErrors()){
            log.info("UserApiController :: modifyPassword :: 유효성 검사 오류 = " + bindingResult.getFieldErrors());
            Map<String, Object> errorMap = new HashMap<>();
            for(FieldError error : bindingResult.getFieldErrors()){
                errorMap.put(error.getField(), error.getDefaultMessage());
            }

            return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.BAD_REQUEST.value()).data(errorMap).message("비밀번호 형식에 맞게 입력하세요요.").build()
                    , HttpStatus.BAD_REQUEST);
        }

        // result = SUCCESS / ERROR
        String result = userService.modifyPassword(modifyPasswordDto);

        // ERROR
        if(result.equals(ReturnResult.ERROR.getValue())){
            throw new UsernameNotFoundException("UserApiController :: modifyPassword :: 존재하는 회원이 아닙니다. ID = " + modifyPasswordDto.getUserId());
        }

        // SUCCESS
        return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.OK.value()).data(ReturnResult.SUCCESS.getValue()).message("비밀번호 변경이 완료되었습니다.").build(), HttpStatus.OK);

    }


}
