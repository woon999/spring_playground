package kr.co.loosie.foody.interfaces;

import kr.co.loosie.foody.domain.User;
import kr.co.loosie.foody.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserService {

    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(String email, String name, String password) {
        User user = User.builder()
                .email(email)
                .name(name)
                .password(password)
                .build();
        return userRepository.save(user);
    }
}
