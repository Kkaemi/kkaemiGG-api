package com.spring.kkaemiGG.exception;

import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends HttpException {

    public UnAuthorizedException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
