package com.work.daily.security.access.controller;

import com.work.daily.access.controller.AccessController;
import com.work.daily.login.service.CustomOAuth2UserService;
import com.work.daily.login.service.CustomUserDetailsService;
import com.work.daily.security.access.annotation.WithMockCustomUser;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccessController.class)
@MockBean(JpaMetamodelMappingContext.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("AccessController :: 단위 테스트")
public class AccessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private CustomOAuth2UserService customOAuth2UserService;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 비회원 메인 페이지 VIEW
     * @description /missions으로 redirect
     * @throws Exception
     */
    @Test
    @Order(1)
    @WithAnonymousUser
    @DisplayName("비회원 - 메인 ViEW 요청 시, /missions로 redirect")
    public void anonymousIndex() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
    }

    /**
     * 회원 메인 페이지 VIEW
     * @description /missions으로 redirect
     * @throws Exception
     */
    @Test
    @Order(2)
    @WithMockUser
    @DisplayName("회원 - 메인 ViEW 요청 시, /missions로 redirect")
    public void userIndex() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
    }

    /**
     * 비회원 로그인 페이지 VIEW
     * @description 로그인 페이지로 이동
     * @throws Exception
     */
    @Test
    @Order(3)
    @WithAnonymousUser
    @DisplayName("비회원 - 로그인 VIEW")
    public void anonymousLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    /**
     * 회원 로그인 페이지 VIEW
     * @dsecription /mission으로 redirect
     * @throws Exception
     */
    @Test
    @Order(4)
    @WithMockCustomUser
    @DisplayName("회원 - 로그인 VIEW 요청 시, /missions로 redirect")
    public void userLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
    }

}
