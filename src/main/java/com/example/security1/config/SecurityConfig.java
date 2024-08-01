package com.example.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // 빈 등록
@EnableWebSecurity //활성화가 되어야 하니까 -> spring security filter(security config)가 filter chain에 등록됨
public class SecurityConfig {
//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//                .authorizeHttpRequests()
//                .requestMatchers("/user/**").authenticated()
//                .requestMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
//                .requestMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
//                .anyRequest().permitAll();
//    }

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
                .formLogin(formLogin -> formLogin.loginPage("/login"))
        ;
        return http.build();
    }

}
