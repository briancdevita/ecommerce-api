package com.example.ecommerce.exception;


import com.example.ecommerce.exception.error.ApiError;
import com.example.ecommerce.exception.error.InsufficientStockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Manejo de excepciones personalizadas (ApiException)
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiError> handleApiException(ApiException ex) {
        ApiError error = ex.getError();
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<Map<String, Object>> handleInsufficientStockException(InsufficientStockException ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", System.currentTimeMillis());
        errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
        errorDetails.put("error", ex.getMessage());
        errorDetails.put("productId", ex.getProductId());
        errorDetails.put("availableStock", ex.getAvailableStock());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("timestamp", System.currentTimeMillis());
        errors.put("status", HttpStatus.BAD_REQUEST.value());
        errors.put("errors", ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
