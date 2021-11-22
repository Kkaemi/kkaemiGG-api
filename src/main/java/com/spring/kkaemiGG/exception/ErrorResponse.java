package com.spring.kkaemiGG.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ErrorResponse {

    private boolean success;
    private int statusCode;
    private String timeStamp;
    private String path;
    private String message;

    public static ErrorResponse of(HttpException httpException, String requestPath) {
        HttpStatus httpStatus = httpException.getHttpStatus();

        return new ErrorResponse(
                false,
                httpStatus.value(),
                LocalDateTime.now().atOffset(ZoneOffset.UTC).toString(),
                requestPath,
                httpException.getMessage()
        );
    }
}
