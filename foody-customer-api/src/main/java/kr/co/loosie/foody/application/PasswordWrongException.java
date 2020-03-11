package kr.co.loosie.foody.application;


public class PasswordWrongException extends RuntimeException {
    PasswordWrongException(){
        super("Password is wrong");
    }
}
