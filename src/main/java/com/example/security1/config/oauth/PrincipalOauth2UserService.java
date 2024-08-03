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
        //registerationId로 어떤 OAuth로 로그인 했는지 확인가능
        System.out.println("getClientRegistration: " + oAuth2UserRequest.getClientRegistration());
        // 사실 엑세스토큰이 없어도 아래에서 getAttributes에서 사용자 정보를 모두 받아올 수 있어서 딱히 필요하지 않음
        System.out.println("getAccessToken: " + oAuth2UserRequest.getAccessToken().getTokenValue());
        // 구글 로그인 버튼 클릭 -> 구글로그인 창 -> 로그인 완료 -> code 리턴(OAuth-Client 라이브러리) -> Access Token 요청
        // userRequest 정보 -> loadUser 함수 호출 -> 구글로부터 회원프로필을 받아줌.
        System.out.println("getAttributes: " + super.loadUser(oAuth2UserRequest).getAttributes());

        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        return super.loadUser(oAuth2UserRequest);
    }
}
