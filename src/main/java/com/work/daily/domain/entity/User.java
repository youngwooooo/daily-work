package com.work.daily.domain.entity;

import com.work.daily.domain.UserRole;
import com.work.daily.domain.pk.UserPK;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
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
    private String userId;

    @Column(columnDefinition = "varchar(300) comment '비밀번호'")
    private String userPw;

    @Column(columnDefinition = "varchar(20) comment '이름'")
    private String userNm;

    @Column(columnDefinition = "varchar(100) comment '이메일'")
    private String userEmail;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(10) comment '권한'")
    private UserRole role;

    @Column(columnDefinition = "varchar(10) comment '소셜타입'")
    private String provider;

    @Column(columnDefinition = "varchar(255) comment '소셜ID'")
    private String providerId;

    @Column(columnDefinition = "varchar(500) comment '프로필사진'")
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

