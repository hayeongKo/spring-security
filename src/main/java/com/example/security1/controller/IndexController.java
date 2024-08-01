package com.example.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller //view를 리턴하겠다
public class IndexController {
    @GetMapping({"", "/"})
    public String index() {
        //mustache 기본폴더 src/main/resources/
        //viewresolver 설정 : templates (prefix).mustache (suffix) -> application.yml에서 설정 => 필수 아님
        //자동으로 경로가 기본적으로 잡힘 -> 생략가능
        return "index"; //src/main/resources/templates/index.mustache 로 경로 탐색하게 됨 -> .html을 못찾음
    }
}
