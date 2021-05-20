package com.example.admin.sample;

import com.example.admin.AdminApplicationTests;

import com.example.admin.model.entity.User;
import com.example.admin.model.enumclass.UserStatus;
import com.example.admin.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Random;

@Slf4j
public class UserSample extends AdminApplicationTests {

    private Random random;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void sampleCreate(){

        random = new Random();

        for(int i = 1 ; i < 1001; i++){
            // 가입 상태 랜덤
            int div = (random.nextInt(10)+1) % 2;
            UserStatus status = (div == 0 ? UserStatus.REGISTERED : UserStatus.UNREGISTERED);

            User user = User.builder()
                    .account("TestUser"+i)
                    .password("password"+i)
                    .status(status)
                    .email("TestUser"+i+"@gmail.com")
                    .phoneNumber("010-1111-"+String.format("%04d", i))
                    .registeredAt(getRandomDate())
                    .unregisteredAt(status.equals(UserStatus.UNREGISTERED) ? getRandomDate() : null )
                    .build();

            userRepository.save(user);
        }


    }


    private LocalDateTime getRandomDate(){
        return LocalDateTime.of(2019,getRandomNumber(),getRandomNumber(),getRandomNumber(),getRandomNumber(),getRandomNumber());
    }

    private int getRandomNumber(){
        return random.nextInt(11)+1;
    }
}
