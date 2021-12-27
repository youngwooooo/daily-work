package com.work.daily.access.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AccessController {

    @GetMapping("/")
    public String index(){
        return "contents/common/index";
    }

    @GetMapping("/login")
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
