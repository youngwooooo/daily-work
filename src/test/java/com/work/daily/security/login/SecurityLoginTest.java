package com.work.daily.security.login;

import com.work.daily.domain.UserRole;
import com.work.daily.domain.entity.User;
import com.work.daily.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Spring Security 로그인 테스트
 * Spring Security 로그인의 경우, 담당하는 Controller 없이 Spring Security가 가로채서 로그인을 한다.
 * CustomUserDetailsService를 통해 검증하므로 @SpringBootTest 사용
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityLoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    public void setup(){
        User user = User.builder()
                .userSeq(1L)
                .userId("loginUser")
                .userPw(bCryptPasswordEncoder.encode("loginuser1234!"))
                .userNm("로그인유저")
                .userEmail("login@gmail.com")
                .role(UserRole.USER)
                .profileImage("profileImage.jpg")
                .build();

        User savedUser = userRepository.save(user);
    }

    /**
     * Form 로그인 테스트
     * @throws Exception
     */
    @Test
    @DisplayName("Form 로그인")
    public void login() throws Exception {
        String userId = "loginUser";
        String password = "loginuser1234!";

        mockMvc.perform(formLogin().userParameter("userId").user(userId).passwordParam("userPw").password(password))
                .andExpect(authenticated())
                .andDo(print());

    }



}
