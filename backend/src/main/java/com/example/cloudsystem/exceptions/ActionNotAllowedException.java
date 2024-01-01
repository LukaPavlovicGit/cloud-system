package com.example.cloudsystem.exceptions;

import org.springframework.http.HttpStatus;

public class ActionNotAllowedException extends CustomException{

    public ActionNotAllowedException(String message) {
        super(message, ErrorCode.METHOD_NOT_ALLOWED, HttpStatus.METHOD_NOT_ALLOWED);
    }
}
