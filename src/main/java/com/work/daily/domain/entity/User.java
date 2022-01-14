package com.work.daily.domain.entity;

import com.work.daily.domain.UserRole;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(value = UserPK.class)
@Entity
@Table(name = "TB_USER_INFO")
public class User extends BaseTime  {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ_INCREASE")
    private long userSeq;

    @Id
    @NotNull
    private String userId;

    // @Column(nullable = false)
    @NotNull
    private String userPw;

    @NotNull
    private String userNm;

    private String userEmail;

    @Enumerated(EnumType.STRING)
    @NotNull
    private UserRole role;

    private String provider;

    private String providerId;

    private String profileImage;

    // 비밀번호 변경을 위함
    public void modifyPassword(String password){
        this.userPw = password;
    }

    // 회원 정보(이름, 이메일, 프로필 사진) 변경을 위함
    public void modifyUserInfo(String name, String email, String profileImage){
        this.userNm = name;
        this.userEmail = email;
        this.profileImage = profileImage;
    }

    @PrePersist
    public void setInsAndUpdUser(){
        this.setInsUser(userId);
        this.setUpdUser(userId);
    }

}

