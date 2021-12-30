package com.work.daily.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserController {

    @GetMapping("/user/mypage/{no}")
    public String mypageForm(@PathVariable long no){
        return "contents/user/mypage";
    }
}
