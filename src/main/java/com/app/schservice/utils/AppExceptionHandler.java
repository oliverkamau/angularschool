package com.app.schservice.utils;

import org.modelmapper.spi.ErrorMessage;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Date;

@ControllerAdvice
public class AppExceptionHandler {


    @ExceptionHandler(value = {Exception.class,})
    public ResponseEntity<Object> handleOtherException(Exception ex, WebRequest request) {
        ex.printStackTrace();
        return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
