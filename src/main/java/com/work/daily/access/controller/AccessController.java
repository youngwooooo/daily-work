package com.work.daily.access.controller;

import com.work.daily.login.auth.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class AccessController {
    /**
     * 메인 페이지
     * @param customUserDetails
     * @return 로그인 : 메인 페이지 VIEW
     *         비로그인 : 전체 미션 VIEW
     */
    @GetMapping({"","/"})
    public String index(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        log.info("AccessController::index called");

        if(customUserDetails != null){
            return "contents/common/index";
        }
        return "contents/mission/missions";
    }

    /**
     * 로그인 페이지
     * @param customUserDetails
     * @return 로그인 : 메인 페이지 VIEW(URL 접속을 막기 위함)
     *         비로그인 : 로그인 페이지 VIEW
     */
    @RequestMapping("/login")
    public String loginForm(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        log.info("AccessController::loginForm called");

        if(customUserDetails == null){
            return "contents/common/login";
        }
        return "contents/common/index";
    }

    /**
     * 회원가입 페이지
     * @return 회원가입 VIEW
     */
    @GetMapping("/join")
    public String joinForm(){
        log.info("AccessController::joinForm called");
        return "contents/common/join";
    }

    /**
     * 회원가입 성공 페이지
     * @return 회원가입 성공 VIEW
     */
    @GetMapping("/join/success")
    public String joinSuccess(){
        log.info("AccessController::joinSuccess called");
        return "contents/common/joinSuccess";
    }

}
