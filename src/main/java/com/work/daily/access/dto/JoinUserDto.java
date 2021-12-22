package com.work.daily.access.dto;

import com.work.daily.domain.UserRole;
import com.work.daily.domain.entity.User;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class JoinUserDto {

    private String id;
    private String password;
    private String name;
    private String email;
    private String phone;
    private UserRole role;

    @Builder
    public JoinUserDto(String id, String password, String name, String email, String phone, UserRole role) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    public User toEntity(){
        return User.builder()
                .id(id)
                .password(password)
                .name(name)
                .email(email)
                .phone(phone)
                .role(role)
                .build();
    }
}
