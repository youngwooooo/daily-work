package com.work.daily.login.dto;

import com.work.daily.domain.UserRole;
import com.work.daily.domain.entity.User;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class LoginUserDto {

    private long userSeq;
    private String userId;
    private String userPw;
    private String userNm;
    private String userEmail;
    private UserRole role;
    private String provider;
    private String profileImage;

    @Builder
    public LoginUserDto(long userSeq, String userId, String userPw, String userNm, String userEmail, UserRole role, String provider, String profileImage) {
        this.userSeq = userSeq;
        this.userId = userId;
        this.userPw = userPw;
        this.userNm = userNm;
        this.userEmail = userEmail;
        this.role = role;
        this.provider = provider;
        this.profileImage = profileImage;
    }

    public User toEntity(){
        return User.builder()
                .userSeq(userSeq)
                .userId(userId)
                .userPw(userPw)
                .userNm(userNm)
                .userEmail(userEmail)
                .role(role)
                .provider(provider)
                .profileImage(profileImage)
                .build();
    }
}
