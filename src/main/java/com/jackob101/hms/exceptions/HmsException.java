package com.jackob101.hms.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HmsException extends RuntimeException{

    private final String code;
    private final Object[] params;
    private final String[] fields;
    private final HttpStatus httpStatus;
    public static final String MESSAGE_DELIMITER = "|";

    public HmsException(String code){
        this.code = code;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.params = new Object[0];
        this.fields = new String[0];
    }

    public HmsException(String code, String[] params) {
        this.code = code;
        this.params = params;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.fields = new String[0];
    }

    public HmsException(String code, HttpStatus httpStatus, String[] params) {
        this.code = code;
        this.params = params;
        this.httpStatus = httpStatus;
        this.fields = new String[0];
    }

    public HmsException(String code, HttpStatus httpStatus, String message, Object[] params, String[] fields) {
        super(message);
        this.code = code;
        this.params = params;
        this.httpStatus = httpStatus;
        this.fields = fields;
    }

    public static DefaultBuilder badRequest() {
        return new HmsBuilder(HttpStatus.BAD_REQUEST);
    }

    public static DefaultBuilder internalError() {
        return new HmsBuilder(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static DefaultBuilder params(Object... params) {
        return badRequest().params(params);
    }

    public static HmsException code(String code) {
        return badRequest().code(code);
    }

    public interface DefaultBuilder {
        DefaultBuilder params(Object... params);

        DefaultBuilder fields(String[] fields);

        HmsException code(String code);
    }


    private static class HmsBuilder implements DefaultBuilder {

        private String detailedMessage;
        private Object[] params;
        private HttpStatus httpStatus;
        private String[] fields;

        public HmsBuilder(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
        }

        public HmsBuilder(String detailedMessage) {
            this.detailedMessage = detailedMessage;
        }

        public HmsBuilder params(Object... params) {
            this.params = params;
            return this;
        }

        public HmsBuilder fields(String[] fields) {
            this.fields = fields;
            return this;
        }

        public HmsException code(String code) {
            if (detailedMessage == null || detailedMessage.isBlank())
                detailedMessage = code;
            return new HmsException(code, httpStatus, detailedMessage, params, fields);
        }

    }
}
