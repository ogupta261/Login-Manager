package com.cg.loginservice.exceptions;

import java.io.IOException;
import java.sql.SQLException;

import com.google.zxing.WriterException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomErrorAdvice {
    
    @ExceptionHandler({ CustomException.class, SQLException.class, NullPointerException.class, WriterException.class, IOException.class})
    public ResponseEntity<ErrorInfo> handle1(Exception ce) {
        System.out.println("..............handleConflict------------...");
        ErrorInfo error = new ErrorInfo(ce.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

}