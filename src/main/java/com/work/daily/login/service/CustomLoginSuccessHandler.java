package com.work.daily.login.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.work.daily.access.ReturnResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
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
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("CustomLoginSuccessHandler 호출");

        ObjectMapper om = new ObjectMapper();
        Map<String, String> map = new HashMap<>();

        /*
            회원이 인증이 필요한 페이지 요청 시, Security가 가로채서 로그인 페이지로 강제로 이동시키는데
            이 때, 사용자가 요청한 모든 자원이 RequestCache 객체에 저장된다.

            RequestCache 객체의 자원들을 통해 SavedRequest 객체를 구현하여
            Security가 로그인을 가로채기 전 회원이 요청한 URL을 알 수 있다.

            즉, Security 로그인 후 회원이 원래 요청했었던 페이지로 이동 시킬 수 있다.
         */
        RequestCache requestCache = new HttpSessionRequestCache();
        SavedRequest savedRequest = requestCache.getRequest(request, response);


        String uri = "/"; // 프로젝트 Context Path
        if(savedRequest != null) {
            uri = savedRequest.getRedirectUrl(); // 회원이 원래 요청했던 페이지
        }

        map.put("result", ReturnResult.SUCCESS.getValue());
        map.put("success", request.getParameter("id"));
        map.put("uri", uri);

        String JsonString = om.writeValueAsString(map);

        OutputStream out = response.getOutputStream();
        out.write(JsonString.getBytes());

    }
}
