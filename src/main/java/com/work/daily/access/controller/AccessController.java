package com.work.daily.access.controller;

import com.work.daily.access.service.AccessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AccessController {

    private final AccessService accessService;

    @RequestMapping(name = "/main")
    public void AccessMain() {
        String userId = "";
        String userPassword = "";

        log.info("AccessController::AccessMain Access...");

        String s = accessService.AccessMain(userId, userPassword);
        System.out.println("s = " + s);

    }
}

