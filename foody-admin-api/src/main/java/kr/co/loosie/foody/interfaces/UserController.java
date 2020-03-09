package kr.co.loosie.foody.interfaces;

import kr.co.loosie.foody.application.UserService;
import kr.co.loosie.foody.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<User> list(){
        List<User> users = userService.getUsers();
        return users;
    }

//    1. User list
//    2. User create -> 회원가입
//    3. User update
//    4. User delete -> level:0 => 아무것도 못함
//    ( 1: customer , 2: restaurant owner , 3: admin)
}
