package com.work.daily.access.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class AccessController {

    @GetMapping({"","/"})
    public String index(){
        log.info("AccessController::index called");
        return "contents/common/index";
    }

    @RequestMapping("/login")
    public String loginForm(){
        log.info("AccessController::loginForm called");
        return "contents/common/login";
    }

    @GetMapping("/join")
    public String joinForm(){
        log.info("AccessController::joinForm called");
        return "contents/common/join";
    }

    @GetMapping("/join/success")
    public String joinSuccess(){
        log.info("AccessController::joinSuccess called");
        return "contents/common/joinSuccess";
    }

}
