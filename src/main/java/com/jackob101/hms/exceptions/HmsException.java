package com.jackob101.hms.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HmsException extends RuntimeException{

    private final String code;
    private final Object[] params;
    private final HttpStatus httpStatus;
    public static final String MESSAGE_DELIMITER = "|";

    public HmsException(String code){
        this.code = code;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.params = new Object[0];
    }

    public HmsException(String code, Object ... params) {
        this.code = code;
        this.params = params;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public HmsException(String code, HttpStatus httpStatus, Object ... params) {
        this.code = code;
        this.params = params;
        this.httpStatus = httpStatus;
    }

    public HmsException(String code, HttpStatus httpStatus, String message, Object ... params) {
        super(message);
        this.code = code;
        this.params = params;
        this.httpStatus = httpStatus;
    }
}
