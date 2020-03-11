package kr.co.loosie.foody.interfaces;


import kr.co.loosie.foody.application.SessionDto;
import kr.co.loosie.foody.application.UserService;
import kr.co.loosie.foody.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class SessionController {

    @Autowired
    private UserService userService;

    @PostMapping("/session")
    public ResponseEntity<SessionDto> create(
            @RequestBody User resource
    ) throws URISyntaxException {
        String accessToken = "ACCESSTOKEN";

        String email = resource.getEmail();
        String password = resource.getPassword();
        userService.authenticate(email,password);

        String url = "/session";
        return ResponseEntity.created(new URI(url)).body(
                SessionDto.builder()
                .accessToken(accessToken)
                .build());
    }

}
