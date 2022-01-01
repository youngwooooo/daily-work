package com.work.daily.domain.entity;

import com.work.daily.domain.UserRole;
import lombok.*;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User extends BaseTime  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long no;

    @Column(name = "user_id", nullable = false, length = 50, unique = true)
    private String id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = true, length = 320)
    private String email;

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
        this.password = password;
    }

    // 회원 정보(이름, 이메일) 변경을 위함
    public void modifyUserInfo(String name, String email){
        this.name = name;
        this.email = email;
    }

}
