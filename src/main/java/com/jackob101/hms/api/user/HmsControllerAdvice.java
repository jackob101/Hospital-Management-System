package com.jackob101.hms.api.user;

import com.jackob101.hms.exceptions.HmsException;
import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.Locale;

@ControllerAdvice
@ResponseBody
public class HmsControllerAdvice {

    private final MessageSource messageSource;

    public HmsControllerAdvice(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(HmsException.class)
    public ResponseEntity<Object> handleException(HmsException ex){

        String message = messageSource.getMessage(ex.getCode(), ex.getParams(), Locale.US);
        ex.printStackTrace();

        JSONObject response = new JSONObject()
                .appendField("timestamp", LocalDateTime.now().toString())
                .appendField("errorCode", ex.getCode())
                .appendField("message", message);

        if(ex.getMessage() != null)
                response.appendField("detailedMessage", ex.getMessage());


        return ResponseEntity.status(ex.getHttpStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toString());

    }
}
