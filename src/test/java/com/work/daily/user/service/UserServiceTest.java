package com.work.daily.user.service;

import com.work.daily.access.ReturnResult;
import com.work.daily.domain.UserRole;
import com.work.daily.domain.entity.User;
import com.work.daily.domain.repository.UserRepository;
import com.work.daily.login.auth.CustomUserDetails;
import com.work.daily.user.dto.ModifyPasswordDto;
import com.work.daily.user.dto.ModifyUserInfoDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("UserService :: 단위 테스트")
public class UserServiceTest {

    @Spy
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void setup(){
        user = User.builder()
                .userSeq(1L)
                .userId("testUser")
                .userPw(bCryptPasswordEncoder.encode("testuser1234!"))
                .userNm("테스트")
                .userEmail("testUser@gmail.com")
                .role(UserRole.USER)
                .profileImage("profile.png")
                .build();
    }

    @Test
    @Order(1)
    @DisplayName("마이페이지 - 계정관리 - 회원정보(이름, 이메일, 프로 필사진) 변경 :: 파일 존재")
    public void modifyUserInfo() throws IOException {
        // given
        ModifyUserInfoDto modifyUserInfoDto = ModifyUserInfoDto.builder()
                                                .userId("testUser")
                                                .userNm("테스트")
                                                .userEmail("testUser@gmail.com").build();

        MockMultipartFile file = new MockMultipartFile("file", "profile.png", MediaType.IMAGE_PNG_VALUE, "image".getBytes());

        given(userRepository.findByUserId(anyString())).willReturn(Optional.ofNullable(user));

        // when
        CustomUserDetails modifyUserDetails = (CustomUserDetails) userService.modifyUserInfo(modifyUserInfoDto, file);

        // then
        assertThat(modifyUserDetails.getLoginUserDto().getProfileImage()).isEqualTo("/profile/" + modifyUserInfoDto.getUserId() + "/" + file.getOriginalFilename());

    }

    @Test
    @Order(2)
    @DisplayName("마이페이지 - 계정관리 - 회원정보(이름, 이메일, 프로 필사진) 변경 :: 파일 Null")
    public void modifyUserInfoFileNull() throws IOException {
        // given
        ModifyUserInfoDto modifyUserInfoDto = ModifyUserInfoDto.builder()
                .userId("testUser")
                .userNm("테스트")
                .userEmail("testUser@gmail.com").build();


        given(userRepository.findByUserId(anyString())).willReturn(Optional.ofNullable(user));

        // when
        CustomUserDetails modifyUserDetailsFileNull = (CustomUserDetails) userService.modifyUserInfo(modifyUserInfoDto, null);

        // then
        assertThat(modifyUserDetailsFileNull.getLoginUserDto().getProfileImage()).isEqualTo("profile.png");

    }

    @Test
    @Order(3)
    @WithMockUser
    @DisplayName("마페이지 - 계정관리 - 비밀번호 확인")
    public void validatingPassword(){
        // given
        ModifyPasswordDto modifyPasswordDto = ModifyPasswordDto.builder()
                                                .userId("testUser")
                                                .userPw("testuser1234!").build();

        given(userRepository.findByUserId(anyString())).willReturn(Optional.ofNullable(user));

        // when
        String result = userService.validatingPassword(modifyPasswordDto);

        // then
        assertThat(result).isEqualTo(ReturnResult.SUCCESS.getValue());
    }

    @Test
    @Order(4)
    @WithMockUser
    @DisplayName("마이페이지 - 계정관리 - 비밀번호 변경")
    public void modifyPassword(){
        // given
        ModifyPasswordDto modifyPasswordDto = ModifyPasswordDto.builder()
                .userId("testUser")
                .userPw("modifypassword1!").build();

        given(userRepository.findByUserId(anyString())).willReturn(Optional.ofNullable(user));

        // when
        String result = userService.modifyPassword(modifyPasswordDto);

        // then
        assertThat(result).isEqualTo(ReturnResult.SUCCESS.getValue());
    }
}
