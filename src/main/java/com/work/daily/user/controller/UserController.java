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

    /**
     * 계정관리(마이페이지) 화면
     * @return 계정관리(마이페이지) 화면
     */
    @GetMapping("/user/mypage")
    public String mypageForm(){
        return "contents/user/mypage";
    }

    /**
     * 계정관리(마이페이지) - 비밀번호 확인 - 비밀번호 변경 화면
     * @return 비밀번호 변경 화면
     */
    @GetMapping("/user/mypage/password")
    public String modifyPasswordForm(){
        return "contents/user/password";
    }

    /**
     * 계정관리(마이페이지) - 비밀번호 확인 - 비밀번호 변경 - 비밀번호 변경 완료 화면
     * @return 비밀번호 변경 완료 화면
     */
    @GetMapping("/user/mypage/password/success")
    public String modifyPasswordSuccess(){
        return "contents/user/modifyPasswordSuccess";
    }


}
