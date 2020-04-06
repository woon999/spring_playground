package com.example.admin.repository;

import com.example.admin.AdminApplicationTests;
import com.example.admin.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public class UserRepositoryTest extends AdminApplicationTests {

    // DI
    @Autowired
    private UserRepository userRepository;

    @Test
    public void create(){
        //String sql = insert into user(%s, %s, %d) value (account, email, age);
        User user = new User();
        user.setAccount("TestUser3");
        user.setEmail("TestUser03@gmail.com");
        user.setPhoneNumber("010-1111-3333");
        user.setCreatedAt(LocalDateTime.now());
        user.setCreatedBy("TestUser3");


        User newUser = userRepository.save(user);
        System.out.println("newUser : " +newUser);
    }

    public void read(){

    }

    public void update(){

    }

    public void delete(){

    }

}