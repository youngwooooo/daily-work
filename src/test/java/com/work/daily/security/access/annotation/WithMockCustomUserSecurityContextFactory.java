package com.work.daily.security.access.annotation;

import com.work.daily.login.auth.CustomUserDetails;
import com.work.daily.login.dto.LoginUserDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

/**
 * Spring Security Session을 생성하는 Class
 */
public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser withMockCustomUser) {
        // createEmptyContext: 직접 SecurityContext 생성하기 위함
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        /*
        * CustomUserDetails를 만들기 위한 파라미터
        * WithMockCustomUser의 저장된 값을 통해 생성
        */
        LoginUserDto loginUserDto = LoginUserDto.builder()
                                        .userId(withMockCustomUser.userId())
                                        .userPw(withMockCustomUser.userPw())
                                        .userNm(withMockCustomUser.userNm())
                                        .role(withMockCustomUser.role())
                                        .provider(withMockCustomUser.profileImage()).build();

        CustomUserDetails customUserDetails = new CustomUserDetails(loginUserDto);

        // CustomUserDetails를 주입받은 Authentication 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, "1234", customUserDetails.getAuthorities());

        // SecurityContext에 Authentication 객체를 저장하여 Spring Security Session을 만듬
        securityContext.setAuthentication(authentication);

        return securityContext;
    }
}
