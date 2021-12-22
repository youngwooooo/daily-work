package com.work.daily.Access.Service;

import com.work.daily.Access.Repository.AccessRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccessService {
    private final AccessRepository accessRepository;

    public String AccessMain(String userId, String userPassword) {
        log.info("AccessController::AccessMain Access...");
        return "OK";
    }
}
