package com.jackob101.hms.api;

import com.jackob101.hms.exceptions.HmsException;
import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;

@ControllerAdvice
@ResponseBody
public class HmsControllerAdvice {

    @ExceptionHandler(HmsException.class)
    public ResponseEntity<Object> handleException(HmsException ex){

        ex.printStackTrace();

        JSONObject response = new JSONObject()
                .appendField("status", ex.getHttpStatus().value())
                .appendField("timestamp", LocalDateTime.now().toString())
                .appendField("message", ex.getMessage());

        if (ex.getFields() != null && ex.getFields().size() > 0) {

            JSONObject fieldErrors = new JSONObject();

            ex.getFields().forEach(fieldError -> fieldErrors.appendField(fieldError.getField(), fieldError.getDefaultMessage()));

            response.appendField("fields", fieldErrors);
        }

        return ResponseEntity.status(ex.getHttpStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toString());

    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Object> handleSqlViolation(SQLIntegrityConstraintViolationException ex) {
        JSONObject response = new JSONObject()
                .appendField("timestamp", LocalDateTime.now().toString())
                .appendField("message", "Unique index or primary key violation exception")
                .appendField("errorCode", ex.getErrorCode());

        ex.printStackTrace();

        return ResponseEntity.internalServerError()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toString());
    }
}
