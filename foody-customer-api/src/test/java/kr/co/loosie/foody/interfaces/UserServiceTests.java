package kr.co.loosie.foody.interfaces;

import kr.co.loosie.foody.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

class UserServiceTests {


    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);

        userService = new UserService(userRepository);
    }


    @Test
    public void registerUser(){

        String email = "tester@example.com";
        String name = "Tester";
        String password = "test";

        userService.registerUser(email,name,password);

        verify(userRepository).save(any());
    }

}