package com.loosie.javaallinone.project3.mycontact.exception.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {
    private int code;
    private String message;

    public static ErrorResponse of(HttpStatus httpStatus, String message){
        return new ErrorResponse(httpStatus.value(),message);
    }
}
