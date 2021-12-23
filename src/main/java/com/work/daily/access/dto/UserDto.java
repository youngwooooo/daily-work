package com.work.daily.access.dto;

import com.work.daily.domain.UserRole;
import com.work.daily.domain.entity.User;
import lombok.*;

@Getter
@Setter
@ToString()
@NoArgsConstructor
public class UserDto {

    private String id;
    private String password;
    private String name;
    private String email;
    private String phone;
    private UserRole role;
    private String provider;
    private String providerId;

    /*
    #####   Builder 패턴 사용 이유   #####
    자바빈즈패턴 : 객체 생성후 상태값을 변경 가능
    빌더패턴    : 객체 생성 후 상태값을 변경 할수 없게 없게 동결 시키는 패턴
     */
    @Builder
    public UserDto(String id, String password, String name, String email, String phone, UserRole role, String provider, String providerId) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
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
                .phone(phone)
                .role(role)
                .provider(provider)
                .providerId(providerId)
                .build();
    }

}
