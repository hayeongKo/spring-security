package com.example.security1.controller;

import com.example.security1.model.User;
import com.example.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller //view를 리턴하겠다
public class IndexController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping({"", "/"})
    public String index() {
        //mustache 기본폴더 src/main/resources/
        //viewresolver 설정 : templates (prefix).mustache (suffix) -> application.yml에서 설정 => 필수 아님
        //자동으로 경로가 기본적으로 잡힘 -> 생략가능
        return "index"; //src/main/resources/templates/index.mustache 로 경로 탐색하게 됨 -> .html을 못찾음
    }

    @GetMapping("/user")
    public @ResponseBody String user() {
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    // spring security가 먼저 낚아챔 -> securityConfig 파일 생성 후 작동 X
//    @GetMapping("/login")
//    public String login() {
//        return "login";
//    }

    @PostMapping("/join")
    public String join(User user) {
        System.out.println(user);
        user.setRole("ROLE_USER");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user); //회원가입은 잘되는데 비밀번호: 1234 -> 시큐리티로 로그인을 할 수 없음.
        //이유는 패스워드가 암호화가 안되었기 때문
        return "redirect:/loginForm";
    }

}
