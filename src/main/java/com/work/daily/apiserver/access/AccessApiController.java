package com.work.daily.apiserver.access;

import com.work.daily.access.dto.UserDto;
import com.work.daily.access.service.AccessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AccessApiController {

    private final AccessService accessService;

    // 회원 가입
    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody UserDto dto){
        accessService.save(dto);
        return new ResponseEntity("success", HttpStatus.OK);

    }
}
