package com.work.daily.login.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.work.daily.access.ReturnResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class CustomLoginFailHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("CustomLoginFailHandler 호출 - 에러 종류 : " + exception);
        ObjectMapper om = new ObjectMapper();
        Map<String, String> map = new HashMap<>();
        String errorMsg = "";

        if(exception instanceof UsernameNotFoundException){
            errorMsg = "존재하는 회원이 아닙니다.";
        }else if(exception instanceof BadCredentialsException){
            errorMsg = "아이디 또는 비밀번호를 확인하세요.";
        }else {
            errorMsg = "알 수 없는 오류로 로그인 할 수 없습니다. 관리자에게 문의하세요";
        }

        map.put("result", ReturnResult.FAIL.getValue());
        map.put("error", exception.getMessage());
        map.put("message", errorMsg);

        String JsonString = om.writeValueAsString(map);

        OutputStream out = response.getOutputStream();
        out.write(JsonString.getBytes());

    }
}
