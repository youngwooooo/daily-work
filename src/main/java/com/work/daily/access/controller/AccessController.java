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
        return "contents/common/index";
    }

    @RequestMapping("/login")
    public String loginForm(){
        return "contents/common/login";
    }

    @GetMapping("/join")
    public String joinForm(){
        return "contents/common/join";
    }

    @GetMapping("/join/success")
    public String joinSuccess(){
        return "contents/common/joinSuccess";
    }

}
