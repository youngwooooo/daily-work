package com.work.daily.login.auth;

import com.work.daily.access.dto.JoinUserDto;
import com.work.daily.login.dto.LoginUserDto;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@ToString
@Getter
public class CustomUserDetails implements UserDetails, OAuth2User {

    private LoginUserDto loginUserDto;
    private Map<String, Object> attributes;
    
    // 일반 form 로그인 생성자
    public CustomUserDetails(LoginUserDto loginUserDto) {
        this.loginUserDto = loginUserDto;
    }
    
    // OAuth 로그인 생성자
    public CustomUserDetails(LoginUserDto loginUserDto, Map<String, Object> attributes) {
        this.loginUserDto = loginUserDto;
        this.attributes = attributes;
    }

    // UserDetails 구현 메서드
    // 비밀번호
    @Override
    public String getPassword() {
        return loginUserDto.getPassword();
    }

    // 아이디
    @Override
    public String getUsername() {
        return loginUserDto.getId();
    }

    // 계정 만료 여부
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠김 여부
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 비밀번호 만료 여부
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정의 활성화 여부
    @Override
    public boolean isEnabled() {
        return true;
    }

    // UserDetails, OAuth2User 공통 구현 메서드
    // 회원의 권한 목록을 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return loginUserDto.getRole().getValue();
            }
        });

        return authorities;
    }

    // OAuth2User 구현 메서드
    // 각 api 별 회원 정보를 가진 map
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    // override를 받는 것뿐 쓰지 않는다.
    @Override
    public String getName() {
        return null;
    }
}
