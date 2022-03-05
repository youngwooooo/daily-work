package com.work.daily.access.service;

import com.work.daily.access.ReturnResult;
import com.work.daily.access.dto.JoinUserDto;
import com.work.daily.domain.entity.User;
import com.work.daily.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AccessServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private AccessService accessService;

    /**
     * save() :: 회원가입 테스트
     * @description save()의 로직 실행 후, 리턴받는 String 확인
     */
    @Test
    @DisplayName("save() :: 회원가입")
    public void save(){
        // given
        JoinUserDto joinUserDto = JoinUserDto.builder()
                                    .userId("testUser")
                                    .userPw("dud1446816@")
                                    .userNm("테스트유저")
                                    .userEmail("test@gmail.com")
                                    .build();


        given(userRepository.findByUserId(anyString())).willReturn(Optional.empty());
        given(userRepository.save(any(User.class))).willReturn(joinUserDto.toEntity());

        // when
        String result = accessService.save(joinUserDto);

        // then
        assertThat(result).isEqualTo(ReturnResult.SUCCESS.getValue());

        // verify
        verify(userRepository).findByUserId(anyString());
        verify(userRepository).save(any(User.class));

    }
}
