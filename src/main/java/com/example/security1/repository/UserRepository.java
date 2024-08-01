package com.example.security1.repository;

import com.example.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

//CRUD 함수를 JPARepository가 들고 있음
//@Repository라는 어노테이션이 없어도 IoC됨. 왜냐 JpaRepository를 상속받았기 때문
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);

    User findByEmail(String email);
}
