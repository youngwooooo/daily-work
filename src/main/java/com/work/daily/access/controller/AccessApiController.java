package com.work.daily.access.controller;

import com.work.daily.access.dto.JoinUserDto;
import com.work.daily.access.service.AccessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public String join(@RequestBody JoinUserDto dto){
        accessService.save(dto);
        return "회원가입 완료";

    }
}
