package com.example.admin.model.enumclass;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserStatus {

    REGISTERED(0,"등록상태","사용자 등록상태"),
    UNREGISTERED(1,"해지","사용자 해자상태");


    private Integer id;
    private String title;
    private String description;
}
