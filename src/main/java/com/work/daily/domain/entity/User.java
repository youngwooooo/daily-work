package com.work.daily.domain.entity;

import com.work.daily.domain.UserRole;
import com.work.daily.domain.embedded.UserPK;
import lombok.*;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(UserPK.class)
@Entity
public class User extends BaseTime  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userSeq;

    @Id
    private String userId;

    @Column(nullable = false)
    private String userPw;

    @Column(nullable = false, length = 10)
    private String userNm;

    @Column(nullable = true, length = 320)
    private String userEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = true)
    private String provider;

    @Column(nullable = true)
    private String providerId;

    @Column(nullable = true)
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

}
