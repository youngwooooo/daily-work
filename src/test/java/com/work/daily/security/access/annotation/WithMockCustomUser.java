package com.work.daily.security.access.annotation;

import com.work.daily.domain.UserRole;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 직접 Custom한 Spring Security Session을 활용하기 위한 Annotation
 * @WithSecurityContext : Spring Security Session을 직접 Custom 하겠다는 의미
 * WithMockCustomUserSecurityContextFactory : Spring Security Session을 생성하는 Class
 */
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

    String userId() default "testUser";
    String userPw() default "testuser1234!";
    String userNm() default "테스트";
    String profileImage() default "profileImage.jpg";
    UserRole role() default UserRole.USER;

}
