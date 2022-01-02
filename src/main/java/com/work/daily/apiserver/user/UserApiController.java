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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;


    /**
     * 계정관리(마이페이지) - 회원 정보(이름, 이메일) 변경
     * @param modifyUserInfoDto
     * @param bindingResult
     * @return
     */
    @PatchMapping("/user/mypage")
    public ResponseEntity<ResponseDto> modifyUserInfo(@RequestBody @Valid ModifyUserInfoDto modifyUserInfoDto, BindingResult bindingResult){
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

        String result = userService.modifyUserInfo(modifyUserInfoDto);

        // ERROR
        if(result.equals(ReturnResult.ERROR.getValue())){
            throw new UsernameNotFoundException("UserApiController :: modifyPassword :: 존재하는 회원이 아닙니다. ID = " + modifyUserInfoDto.getId());
        }

        // 세션 수정
        Authentication newAuthentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(modifyUserInfoDto.getId(), null));
        SecurityContextHolder.getContext().setAuthentication(newAuthentication);

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
            throw new UsernameNotFoundException("UserApiController :: passwordModifyForm :: 존재하는 회원이 아닙니다. ID = " + modifyPasswordDto.getId());
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
            throw new UsernameNotFoundException("UserApiController :: modifyPassword :: 존재하는 회원이 아닙니다. ID = " + modifyPasswordDto.getId());
        }

        // SUCCESS
        return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.OK.value()).data(ReturnResult.SUCCESS.getValue()).message("비밀번호 변경이 완료되었습니다.").build(), HttpStatus.OK);

    }


}
