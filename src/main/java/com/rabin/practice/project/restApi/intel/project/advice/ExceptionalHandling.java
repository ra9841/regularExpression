package com.rabin.practice.project.restApi.intel.project.advice;

import com.rabin.practice.project.restApi.intel.project.exception.UserAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionalHandling {

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<Map<String,String>> ExceptionHandlerMethod(UserAlreadyExistException ex){
        Map<String,String> mapError=new HashMap<>();
        mapError.put("message", ex.getMessage());
        mapError.put("status", HttpStatus.NOT_FOUND.toString());
        return ResponseEntity.ok(mapError);
    }
}
