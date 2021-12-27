package com.work.daily.login.auth.provider;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {

    private String providerId;
    private Map<String, Object> kakaoAccount;
    private Map<String, Object> kakaoProfile;

    public KakaoUserInfo(String providerId, Map<String, Object> kakaoAccount) {
        this.providerId = providerId;
        this.kakaoAccount = kakaoAccount;
        this.kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");
    }

    @Override
    public String getProviderId() {
        return providerId;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        return (String) kakaoAccount.get("email");
    }

    @Override
    public String getName() {
        return (String) kakaoProfile.get("nickname");
    }
}
