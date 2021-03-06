package com.work.daily.security.access.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.work.daily.access.ReturnResult;
import com.work.daily.access.dto.JoinUserDto;
import com.work.daily.access.service.AccessService;
import com.work.daily.apiserver.access.AccessApiController;
import com.work.daily.config.SecurityConfig;
import com.work.daily.domain.UserRole;
import com.work.daily.domain.entity.User;
import com.work.daily.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.AuthenticatedMatcher;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * AccessApiController ?????????
 * @description @WebMvcTest ???????????? AccessApiController??? return ?????? ????????????.
 */
@WebMvcTest(controllers = AccessApiController.class // ????????? Controller ???????????? ??????
            , excludeFilters = {
                    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)}) // Spring security ?????? ??????
@MockBean(JpaMetamodelMappingContext.class) // @EnableJpaAuditing??? ??????????????? ???
@DisplayName("AccessApiController :: ?????? ?????????")
public class AccessApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean(name = "AccessService")
    private AccessService accessService;

    /**
     * join() :: ???????????? ?????????
     * @description join()??? accessService.save()??? ?????? ???, ???????????? ResponseEntity??? ?????? ??????
     * @throws Exception
     */
    @Test
    @WithMockUser
    @DisplayName("join() - ???????????? Controller")
    public void join() throws Exception {
        /**
         * given
         * (1) : JoinUserDto ?????? ??????
         * (2) : joinUserDto??? Json ???????????? ??????
         * (3) : ?????? ????????? ?????? ???, SUCCESS??? return ????????? ???
         *      * any(JoinUserDto.class) : NullPointException??? ???????????? ??????
         *                                 ?????? JoinUserDto ????????? ???????????? SUCCESS??? ?????????.
         */
        // (1)
        JoinUserDto joinUserDto = JoinUserDto.builder()
                                    .userId("testUser")
                                    .userPw("dud1446816@")
                                    .userNm("???????????????")
                                    .userEmail("test@gmail.com")
                                    .build();
        // (2)
        String content = new ObjectMapper().writeValueAsString(joinUserDto);
        // (3)
        given(accessService.save(any(JoinUserDto.class))).willReturn(ReturnResult.SUCCESS.getValue());

        /**
         * when & then
         * (1) Spring Security csrf ?????? : 403 ????????? ?????? ??????
         * (2) response?????? status ????????? ??????
         * (3),(4) response?????? body??? ???????????? ??????
         */
        mockMvc.perform(post("/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())) // (1)
                .andExpect(status().isCreated()) // (2)
                .andExpect(jsonPath("$.status").value(201)) // (3)
                .andExpect(jsonPath("$.data").value(ReturnResult.SUCCESS.getValue())) // (4)
                .andDo(print());

        verify(accessService).save(any(JoinUserDto.class));

    }

}
