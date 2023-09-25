package com.rabin.practice.project.restApi.intel.project.advice;

import com.rabin.practice.project.restApi.intel.project.exception.UserAlreadyExistException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ExceptionalHandlingTest {

    private ExceptionalHandling exceptionalHandlingUnderTest;

    @BeforeEach
    void setUp() {
        exceptionalHandlingUnderTest = new ExceptionalHandling();
    }

    @Test
    void testExceptionHandlerMethod() {
        // Setup
        final UserAlreadyExistException ex = new UserAlreadyExistException("message");
        final ResponseEntity<Map<String, String>> expectedResult = new ResponseEntity<>(
                Map.ofEntries(Map.entry("message","message"),Map.entry("status","404 NOT_FOUND")), HttpStatusCode.valueOf(200));

        // Run the test
        final ResponseEntity<Map<String, String>> result = exceptionalHandlingUnderTest.ExceptionHandlerMethod(ex);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testHandleInvalidArgument() {
        // Setup
        final ConstraintViolationException ex = new ConstraintViolationException("message", Set.of());
        final ResponseEntity<Map<String, String>> expectedResult = new ResponseEntity<>(
                Map.ofEntries(), HttpStatusCode.valueOf(400));

        // Run the test
        final ResponseEntity<Map<String, String>> result = exceptionalHandlingUnderTest.handleInvalidArgument(ex);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }
}
