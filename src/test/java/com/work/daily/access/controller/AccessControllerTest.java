package com.work.daily.access.controller;

import com.work.daily.config.SecurityConfig;
import com.work.daily.access.security.WithMockCustomUser;
import com.work.daily.login.service.CustomOAuth2UserService;
import com.work.daily.login.service.CustomUserDetailsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccessController.class
            , includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class))
@MockBean(JpaMetamodelMappingContext.class)
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
    @WithAnonymousUser
    @DisplayName("비회원 :: 메인 ('', /) -> redirect /mission")
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
    @WithMockUser
    @DisplayName("회원 :: 메인('', /) -> redirect /mission")
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
    @WithAnonymousUser
    @DisplayName("비회원 :: /login")
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
    @WithMockCustomUser
    @DisplayName("회원 :: /login -> redirect /mission")
    public void userLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
    }

}
