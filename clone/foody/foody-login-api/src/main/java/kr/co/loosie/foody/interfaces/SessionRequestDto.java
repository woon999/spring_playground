package kr.co.loosie.foody.interfaces;

import lombok.Data;

@Data
public class SessionRequestDto {

    private String email;
    private String password;
}
