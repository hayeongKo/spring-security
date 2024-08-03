package com.example.security1.config.oauth;

import com.example.security1.config.auth.PrincipalDetails;
import com.example.security1.model.User;
import com.example.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    // 구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수
    // 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어짐
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        //registerationId로 어떤 OAuth로 로그인 했는지 확인가능
        System.out.println("getClientRegistration: " + oAuth2UserRequest.getClientRegistration());
        // 사실 엑세스토큰이 없어도 아래에서 getAttributes에서 사용자 정보를 모두 받아올 수 있어서 딱히 필요하지 않음
        System.out.println("getAccessToken: " + oAuth2UserRequest.getAccessToken().getTokenValue());

        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        // 구글 로그인 버튼 클릭 -> 구글로그인 창 -> 로그인 완료 -> code 리턴(OAuth-Client 라이브러리) -> Access Token 요청
        // userRequest 정보 -> loadUser 함수 호출 -> 구글로부터 회원프로필을 받아줌.
        System.out.println("getAttributes: " + oAuth2User.getAttributes());

        //회원가입을 강제로 진행해볼 예정
        String provider = oAuth2UserRequest.getClientRegistration().getClientId();//google
        String providerId = oAuth2User.getAttribute("sub").toString();
        String username = provider + "_" + providerId; //google_xxxxxxxxxxx -> username 충돌 방지
//        String password = bCryptPasswordEncoder.encode("겟인데어");
        String email = oAuth2User.getAttribute("email").toString();
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUsername(username);

        if (userEntity == null) {
            System.out.println("최초 로그인");
            userEntity = User.builder()
                    .username(username)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        } else {
            System.out.println("이미 회원가입이 완료됨");
        }

        return new PrincipalDetails(userEntity, oAuth2User.getAttributes()); //Authentication 객체에 들어감 -> user와 attributes
    }
}
