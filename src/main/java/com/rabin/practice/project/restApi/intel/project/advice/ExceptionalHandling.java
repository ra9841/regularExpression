package com.rabin.practice.project.restApi.intel.project.advice;

import com.rabin.practice.project.restApi.intel.project.exception.UserAlreadyExistException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String,String>> handleInvalidArgument(ConstraintViolationException ex){
        Map<String, String> errorMap = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errorMap.put(fieldName, errorMessage);
        }
        return ResponseEntity.badRequest().body(errorMap);
    }


}
