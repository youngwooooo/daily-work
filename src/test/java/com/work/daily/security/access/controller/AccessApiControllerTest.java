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
 * AccessApiController 테스트
 * @description @WebMvcTest 활용하여 AccessApiController의 return 값을 확인한다.
 */
@WebMvcTest(controllers = AccessApiController.class // 단순한 Controller 테스트를 위함
            , excludeFilters = {
                    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)}) // Spring security 설정 제회
@MockBean(JpaMetamodelMappingContext.class) // @EnableJpaAuditing를 인식하도록 함
@DisplayName("AccessApiController :: 단위 테스트")
public class AccessApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean(name = "AccessService")
    private AccessService accessService;

    /**
     * join() :: 회원가입 테스트
     * @description join()의 accessService.save()를 처리 후, 리턴받는 ResponseEntity의 값을 확인
     * @throws Exception
     */
    @Test
    @WithMockUser
    @DisplayName("join() - 회원가입 Controller")
    public void join() throws Exception {
        /**
         * given
         * (1) : JoinUserDto 객체 생성
         * (2) : joinUserDto를 Json 타입으로 변환
         * (3) : 해당 서비스 실행 시, SUCCESS를 return 하도록 함
         *      * any(JoinUserDto.class) : NullPointException를 처리하기 위함
         *                                 어떤 JoinUserDto 객체가 오더라도 SUCCESS를 리턴함.
         */
        // (1)
        JoinUserDto joinUserDto = JoinUserDto.builder()
                                    .userId("testUser")
                                    .userPw("dud1446816@")
                                    .userNm("테스트유저")
                                    .userEmail("test@gmail.com")
                                    .build();
        // (2)
        String content = new ObjectMapper().writeValueAsString(joinUserDto);
        // (3)
        given(accessService.save(any(JoinUserDto.class))).willReturn(ReturnResult.SUCCESS.getValue());

        /**
         * when & then
         * (1) Spring Security csrf 토큰 : 403 에러를 막기 위함
         * (2) response되는 status 코드와 비교
         * (3),(4) response되는 body의 데이터와 비교
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
