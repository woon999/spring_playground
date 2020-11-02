package com.study.Jpawebapp.account;


import lombok.Data;

/**
 * 회원가입할 때 받아올 데이
 */
@Data
public class SignUpForm {

    private String nickname;

    private String email;

    private String password;
}
