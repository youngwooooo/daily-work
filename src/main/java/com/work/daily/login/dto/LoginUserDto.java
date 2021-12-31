package com.work.daily.login.dto;

import com.work.daily.domain.UserRole;
import com.work.daily.domain.entity.User;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class LoginUserDto {

    private long no;
    private String id;
    private String password;
    private String name;
    private String email;
    private UserRole role;
    private String provider;
    private String profileImage;

    @Builder
    public LoginUserDto(long no, String id, String password, String name, String email, UserRole role, String provider, String profileImage) {
        this.no = no;
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.profileImage = profileImage;
    }

    public User toEntity(){
        return User.builder()
                .no(no)
                .id(id)
                .password(password)
                .name(name)
                .email(email)
                .role(role)
                .provider(provider)
                .profileImage(profileImage)
                .build();
    }
}
