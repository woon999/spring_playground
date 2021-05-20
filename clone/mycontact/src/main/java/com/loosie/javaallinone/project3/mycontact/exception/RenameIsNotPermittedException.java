package com.loosie.javaallinone.project3.mycontact.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RenameIsNotPermittedException extends RuntimeException {
    private static final String MESSAGE = "이름 변경이 허용되지 않습니다.";

    public RenameIsNotPermittedException(){
        super(MESSAGE);
        log.error(MESSAGE);
    }
}
