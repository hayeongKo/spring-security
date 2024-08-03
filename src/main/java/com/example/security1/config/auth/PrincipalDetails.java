package com.example.security1.config.auth;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킴
// 로그인 진행이 완료되면 시큐리티 session을 만들어줌 -> 같은 세션 공간인데 시큐리티가 자신만의 세션 공간을 가짐 (키 값으로 구분 = security ContextHolder)
// 세션이 들어갈 수 있는 오브젝트 => Authentication 타입 객체
// Authentication 안에 User 정보가 있어야 됨
// User 오브젝트 타입 -> UserDetails 타입 객체

import com.example.security1.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

// Security Session > Authentication > UserDetails(=PrincipalDetails)
@Data
public class PrincipalDetails implements UserDetails {

    private User user; //composition

    public PrincipalDetails(User user) {
        this.user = user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 계정 만료되지 않았니 ?
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠겨있지는 않니 ?
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 계정 비밀번호를 바꿀 때가 되지 않았니 ?
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정 활성화 되어있음 ?
    @Override
    public boolean isEnabled() {
        //우리 사이트에서 1년동안 회원이 로긘 안하면 -> 휴먼 계정으로 하기로 함
        // user에 loginDate라는 column을 만들고 해당 데이터를 이용하여 계산 후 로직 실행
        return true;
    }

    // 해당 User의 권한을 리턴해줌
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>(); //arrayList는 collection의 자식
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

}
