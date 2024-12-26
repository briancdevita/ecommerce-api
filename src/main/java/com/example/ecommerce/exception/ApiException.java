package com.example.ecommerce.exception;


import com.example.ecommerce.exception.error.ApiError;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Data
public class ApiException extends  RuntimeException{

    private final ApiError error;


    public HttpStatus getStatus() {
        return error.getStatus();
    }





}
