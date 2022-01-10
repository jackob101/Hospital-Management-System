package com.jackob101.hms.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HmsException extends RuntimeException{

    private final Object[] params;
    private final String[] fields;
    private final HttpStatus httpStatus;
    public static final String MESSAGE_DELIMITER = "|";

    private HmsException() {
        this.params = new Object[0];
        this.fields = new String[0];
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public HmsException(String code, HttpStatus httpStatus, Object[] params, String[] fields) {
        super(String.format(code, params));
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
        private final HttpStatus httpStatus;
        private String[] fields;

        public HmsBuilder(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
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
            return new HmsException(code, httpStatus, params, fields);
        }

    }
}
