package com.example.security1.config;

import com.example.security1.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // 빈 등록
@EnableWebSecurity //활성화가 되어야 하니까 -> spring security filter(security config)가 filter chain에 등록됨
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
//@Secured 활성화, @PreAuthorize & @PostAuthorize 활성화
public class SecurityConfig {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    //해당 메서드의 리턴되는 오브젝트를 IoC로 등록해줌
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    //spring boot 3 이후부터 WebSecurityConfigurerAdapter deprecated
    //따라서 따로 필터체인을 정의하여 사용해야함
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf((csrfConfig) -> csrfConfig.disable()) //추가로 메서드 체이닝이 아닐 람다식으로 사용하도록 deprecated됨
                .authorizeHttpRequests(authorize -> authorize
                        // URL 패턴별 접근 권한 설정
                        .requestMatchers("/user/**").authenticated() // /user/** 경로는 인증된 사용자만 접근 가능
                        .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER") // /manager/** 경로는 ROLE_ADMIN 또는 ROLE_MANAGER 권한을 가진 사용자만 접근 가능
                        .requestMatchers("/admin/**").hasRole("ADMIN") // /admin/** 경로는 ROLE_ADMIN 권한을 가진 사용자만 접근 가능
                        .anyRequest().permitAll() // 그 외의 모든 요청은 인증 없이 접근 가능
                )
                .formLogin(formLogin -> {
                    formLogin.loginPage("/loginForm");
                    formLogin.loginProcessingUrl("/login"); // /login 주소가 호출되면 security가 낚아채서 대신 로그인을 진행해줌 -> controller /login을 만들지 않아도 됨
                    formLogin.defaultSuccessUrl("/"); //성공시 리다이렉트할 url
                })
                // 구글 로그인 완료 뒤 후처리 구현해야함. Tip.코드 X, (엑세스토큰+사용자프로필정보 O)
                // ----------------------------------------------------------------
                // 1. 코드받기(인증) 2. 엑세스토큰
                // 3. 사용자프로필 정보를 가져오고
                // 4-1. 그 정보를 토대로 회원가입을 자동으로 진행시키기도 함
                // 4-2. (이메일, 전화번호, 이름, 아이디) 쇼핑몰 -> 지부소, 백화점몰 -> (vip등급, 일반등급)
                .oauth2Login(oauth2Login -> {
                            oauth2Login.loginPage("/loginForm");
                            oauth2Login.userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                    .userService(principalOauth2UserService));
                        }
                )
        ;
        return http.build();
    }

}
