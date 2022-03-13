package com.work.daily.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.work.daily.access.ReturnResult;
import com.work.daily.apiserver.user.UserApiController;
import com.work.daily.config.SecurityConfig;
import com.work.daily.domain.UserRole;
import com.work.daily.login.auth.CustomUserDetails;
import com.work.daily.login.dto.LoginUserDto;
import com.work.daily.user.dto.ModifyPasswordDto;
import com.work.daily.user.dto.ModifyUserInfoDto;
import com.work.daily.user.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserApiController.class
            , excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
@MockBean(JpaMetamodelMappingContext.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("UserApiController :: 단위 테스트")
public class UserApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean(name = "UserService")
    private UserService userService;

    @Test
    @Order(1)
    @WithMockUser
    @DisplayName("마이페이지 - 계정관리 - 회원정보(이름, 이메일, 프로필 사진) 수정")
    public void modifyUserInfo() throws Exception {
        // given
        ModifyUserInfoDto modifyUserInfoDto = ModifyUserInfoDto.builder()
                                                .userId("testUser")
                                                .userNm("테스트")
                                                .userEmail("testuser@gmail.com")
                                                .build();

        MockMultipartFile file = new MockMultipartFile("file", "profile.png", MediaType.IMAGE_PNG_VALUE, "image".getBytes());

        String content = new ObjectMapper().writeValueAsString(modifyUserInfoDto);
        // @RequestPart로 받는 ModifyUserInfoDto 객체는 해당 테스트에서 파일 객체로 만들어야 인식을 할 수 있다.
        MockMultipartFile modifyUserInfoDtoJson = new MockMultipartFile("modifyUserInfoDto", "jsonData", MediaType.APPLICATION_JSON_VALUE, content.getBytes(StandardCharsets.UTF_8));

        // CustomUserDetails 객체를 만들기 위한 LoginUserDto
        LoginUserDto loginUserDto = LoginUserDto.builder()
                                        .userSeq(1L)
                                        .userId("testUser")
                                        .userPw("testuser1234!")
                                        .userNm("유저1")
                                        .userEmail("testuser@gmail.com")
                                        .role(UserRole.USER)
                                        .profileImage(file.getOriginalFilename())
                                        .build();

        UserDetails modifyUserDetails = new CustomUserDetails(loginUserDto);
        given(userService.modifyUserInfo(any(ModifyUserInfoDto.class), any(MultipartFile.class))).willReturn(modifyUserDetails);

        /*
            multipart()의 Http Method는 기본적으로 POST
            하지만 해당 URL에 매핑된 Http Method는 PATCH이므로, POST -> PATCH로 변경해주는 작업이 필요함
            MockMultipartHttpServletRequestBuilder 객체를 생성하여 multipart()를 선언해주고 해당 Http Method를 PATCH로 바꿔준다.
         */
        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/user/my-account");
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PATCH");
                return request;
            }
        });

        // when & then
        mockMvc.perform(builder
                        .file(file) // @RequestPart(value = "file", required = false) MultipartFile file
                        .file(modifyUserInfoDtoJson) // @RequestPart(value = "modifyUserInfoDto") ModifyUserInfoDto modifyUserInfoDto
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(ReturnResult.SUCCESS.getValue()))
                .andDo(print());

        verify(userService).modifyUserInfo(any(ModifyUserInfoDto.class), any(MultipartFile.class));

    }

    @Test
    @Order(2)
    @WithMockUser
    @DisplayName("마이페이지 - 계정관리 - 비밀번호 확인")
    public void validatingPassword() throws Exception {
        // given
        ModifyPasswordDto modifyPasswordDto = ModifyPasswordDto.builder()
                                                .userId("testUser")
                                                .userPw("modifypassword123!").build();

        String content = new ObjectMapper().writeValueAsString(modifyPasswordDto);

        given(userService.validatingPassword(any(ModifyPasswordDto.class))).willReturn(ReturnResult.SUCCESS.getValue());

        // when & then
        mockMvc.perform(post("/user/my-account/valid/password")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
                    .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(ReturnResult.SUCCESS.getValue()))
                .andDo(print());

        verify(userService).validatingPassword(any(ModifyPasswordDto.class));
    }

    @Test
    @Order(3)
    @WithMockUser
    @DisplayName("마이페이지 - 계졍관리 - 비밀번호 변경")
    public void modifyPassword() throws Exception {
        // given
        ModifyPasswordDto modifyPasswordDto = ModifyPasswordDto.builder()
                                                .userId("testUser")
                                                .userPw("modifypassword123!").build();

        String content = new ObjectMapper().writeValueAsString(modifyPasswordDto);

        given(userService.modifyPassword(any(ModifyPasswordDto.class))).willReturn(ReturnResult.SUCCESS.getValue());

        // when & then
        mockMvc.perform(patch("/user/my-account/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(ReturnResult.SUCCESS.getValue()))
                .andDo(print());

        verify(userService).modifyPassword(any(ModifyPasswordDto.class));
    }



}
