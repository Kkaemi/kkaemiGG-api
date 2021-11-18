package com.spring.kkaemiGG.exception;

import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends HttpException {

    public InternalServerErrorException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
