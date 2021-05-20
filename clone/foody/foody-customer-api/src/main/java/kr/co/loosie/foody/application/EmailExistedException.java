package kr.co.loosie.foody.application;

public class EmailExistedException extends RuntimeException {

     public EmailExistedException(String email){
          super("Email is already registered: " + email);
     }

}
