package com.spring.kkaemiGG.web.controller;

import com.spring.kkaemiGG.exception.ErrorResponse;
import com.spring.kkaemiGG.exception.HttpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<ErrorResponse> handleHttpException(
            HttpServletRequest request,
            HttpException httpException
    ) {
        HttpStatus httpStatus = httpException.getHttpStatus();
        ErrorResponse body = ErrorResponse.of(httpException, request.getRequestURI());

        return ResponseEntity.status(httpStatus)
                .body(body);
    }
}
