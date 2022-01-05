package com.jackob101.hms.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HmsException extends RuntimeException{

    private final ExceptionCode exceptionCode;
    private final HttpStatus httpStatus;

    public HmsException(String message, ExceptionCode exceptionCode, HttpStatus httpStatus) {
        super(message);
        this.exceptionCode = exceptionCode;
        this.httpStatus = httpStatus;
    }
}
