package com.jackob101.hms.api.user;

import com.jackob101.hms.exceptions.HmsException;
import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

@ControllerAdvice
@ResponseBody
public class HmsControllerAdvice {

    @org.springframework.web.bind.annotation.ExceptionHandler(HmsException.class)
    public ResponseEntity<Object> handleException(HmsException ex){

        String response = new JSONObject()
                .appendField("timestamp", LocalDateTime.now().toString())
                .appendField("errorCode", ex.getExceptionCode().code)
                .appendField("exceptionMessage", ex.getExceptionCode().message)
                .appendField("detailedMessage", ex.getMessage())
                .toJSONString();

        return ResponseEntity.status(ex.getHttpStatus())
                .body(response);

    }
}
