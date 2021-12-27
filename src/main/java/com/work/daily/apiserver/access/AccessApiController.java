package com.work.daily.apiserver.access;

import com.work.daily.access.ReturnResult;
import com.work.daily.access.dto.JoinResponseDto;
import com.work.daily.access.dto.UserDto;
import com.work.daily.access.service.AccessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AccessApiController {

    private final AccessService accessService;

    /**
     * 회원가입
     * @param userDto
     * @param bindingResult
     * @return JoinResponseDto(HttpStatus.value(), String message, Object data)
     */
    @PostMapping("/join")
    public ResponseEntity<JoinResponseDto> join(@RequestBody @Valid UserDto userDto, BindingResult bindingResult) {
        // 유효성 검사 후 에러가 발생한 경우
        if(bindingResult.hasErrors()){
            Map<String, Object> errorMap = new HashMap<>();
            for(FieldError error : bindingResult.getFieldErrors()){
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(JoinResponseDto.builder().status(HttpStatus.BAD_REQUEST.value()).message("입력 값이 올바르지 않습니다.").data(errorMap).build()
                                        , HttpStatus.BAD_REQUEST);
        }
        // result = SUCCESS / FAIL
        String result = accessService.save(userDto);
        if(result.equals(ReturnResult.FAIL.getValue())){
            return new ResponseEntity<>(JoinResponseDto.builder().status(HttpStatus.CONFLICT.value()).message("이미 존재하는 회원입니다.").data(result).build()
                                        , HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(JoinResponseDto.builder().status(HttpStatus.CREATED.value()).message("회원가입이 완료되었습니다.").data(result).build()
                                    , HttpStatus.CREATED);

    }
}
