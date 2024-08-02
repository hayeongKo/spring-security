package com.example.security1.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    // 구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        System.out.println("getClientRegistration: " + oAuth2UserRequest.getClientRegistration());
        // 사실 엑세스토큰이 없어도 아래에서 getAttributes에서 사용자 정보를 모두 받아올 수 있어서 딱히 필요하지 않음
        System.out.println("getAccessToken: " + oAuth2UserRequest.getAccessToken().getTokenValue());
        System.out.println("getAttributes: " + super.loadUser(oAuth2UserRequest).getAttributes());
        return super.loadUser(oAuth2UserRequest);
    }
}
