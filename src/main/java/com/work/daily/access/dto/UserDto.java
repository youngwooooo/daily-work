package com.work.daily.access.dto;

import com.work.daily.domain.UserRole;
import com.work.daily.domain.entity.User;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserDto {

    @NotBlank(message = "아이디를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[a-z])[A-Za-z0-9]{4,20}$", message = "아이디 형식에 맞게 입력해주세요.")
    private String id;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{10,32}", message = "비밀번호 형식에 맞게 입력해주세요.")
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")
    @Pattern(regexp = "^[가-힣]{2,6}$", message = "이름 형식에 맞게 입력해주세요.")
    private String name;

    @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$", message = "이메일 형식에 맞게 입력해주세요.")
    private String email;

    private UserRole role;
    private String provider;
    private String providerId;

    /*
    #####   Builder 패턴 사용 이유   #####
    자바빈즈패턴 : 객체 생성후 상태값을 변경 가능
    빌더패턴    : 객체 생성 후 상태값을 변경 할수 없게 없게 동결 시키는 패턴
     */
    @Builder
    public UserDto(String id, String password, String name, String email, UserRole role, String provider, String providerId) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }

    public User toEntity(){
        return User.builder()
                .id(id)
                .password(password)
                .name(name)
                .email(email)
                .role(role)
                .provider(provider)
                .providerId(providerId)
                .build();
    }

}