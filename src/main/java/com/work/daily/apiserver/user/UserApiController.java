package com.work.daily.apiserver.user;

import com.work.daily.access.ReturnResult;
import com.work.daily.access.dto.ResponseDto;
import com.work.daily.domain.entity.User;
import com.work.daily.domain.repository.UserRepository;
import com.work.daily.user.dto.ModifyPasswordDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    /**
     * 계정 관리 - 비밀번호 변경 - 비밀번호 확인
     * @param modifyPasswordDto
     * @param bindingResult
     * @return
     */
    @PostMapping("/user/mypage/valid/password")
    public ResponseEntity<ResponseDto> validatingPassword(@RequestBody @Valid ModifyPasswordDto modifyPasswordDto, BindingResult bindingResult){
        log.info("UserApiController :: passwordModifyForm called");

        log.info("modifyPasswordDto : " + modifyPasswordDto);
        log.info("bindingResult : " + bindingResult);

        // 유효성 검사 오류 발생 시
        if(bindingResult.hasErrors()){
            Map<String, Object> errorMap = new HashMap<>();
            for(FieldError error : bindingResult.getFieldErrors()){
                errorMap.put(error.getField(), error.getDefaultMessage());
            }

            return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.BAD_REQUEST.value()).data(errorMap).message("비밀번호가 일치하지 않습니다.").build()
                                        , HttpStatus.BAD_REQUEST);
        }


        Optional<User> findUser = userRepository.findById(modifyPasswordDto.getId());
        log.info("findUser : " + findUser.get().toString());

        // 비밀번호가 같을 경우
        if(bCryptPasswordEncoder.matches(modifyPasswordDto.getPassword(), findUser.get().getPassword())){
            return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.OK.value()).data(ReturnResult.SUCCESS.getValue()).message("비밀번호가 일치합니다.").build(), HttpStatus.OK);
        }

        return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.BAD_REQUEST.value()).data(ReturnResult.FAIL.getValue()).message("비밀번호가 일치하지 않습니다.").build()
                                    , HttpStatus.BAD_REQUEST);
    }
}
