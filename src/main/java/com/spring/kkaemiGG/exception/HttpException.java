package com.spring.kkaemiGG.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HttpException extends Exception {

    private final HttpStatus httpStatus;

    public HttpException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
