package com.work.daily.user.repository;

import com.work.daily.TestConfig;
import com.work.daily.domain.UserRole;
import com.work.daily.domain.entity.User;
import com.work.daily.domain.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(TestConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("UserRepository :: 단위 테스트")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    @DisplayName("회원 생성")
    public void save(){
        // given
        User user = User.builder()
                            .userId("saveUser")
                            .userNm("생성된회원")
                            .userEmail("saveUser@gmail.com")
                            .userPw("saveUser1234!")
                            .role(UserRole.USER)
                            .profileImage("profileImage.png")
                            .build();
        // when
        User savedUser = userRepository.save(user);
        userRepository.flush();

        // then
        assertThat(savedUser.getUserSeq()).isEqualTo(1);
        assertThat(savedUser.getUserId()).isEqualTo(user.getUserId());
        assertThat(savedUser.getRole()).isEqualTo(user.getRole());
    }

    @Test
    @Order(2)
    @DisplayName("회원 ID로 회원 정보 조회")
    public void findByUserId() {
        // given
        User user = User.builder()
                .userId("saveUser")
                .userNm("생성된회원")
                .userEmail("saveUser@gmail.com")
                .userPw("saveUser1234!")
                .role(UserRole.USER)
                .profileImage("profileImage.png")
                .build();

        User savedUser = userRepository.save(user);

        // when
        Optional<User> findUser = userRepository.findByUserId(user.getUserId());

        // then
        assertThat(findUser.get().getUserId()).isEqualTo(user.getUserId());

    }
}