package com.rom.global.config.security.oauth;

import com.rom.domain.user.domain.Provider;
import com.rom.global.DefaultAssert;
import com.rom.global.config.security.oauth.company.Google;
import com.rom.global.config.security.oauth.company.Naver;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase(Provider.google.toString())) {
            return new Google(attributes);
        } else if (registrationId.equalsIgnoreCase(Provider.naver.toString())) {
            return new Naver(attributes);
        } else {
            DefaultAssert.isAuthentication("해당 oauth2 기능은 지원하지 않습니다.");
        }
        return null;
    }
}