package com.sumantha.loadBalancerMock.exception;


import com.sumantha.loadBalancerMock.dto.response.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.sumantha.loadBalancerMock.constants.AppConstants.RESOURCE_NOT_FOUND;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseDTO<String>> handleResourceNotFoundException(CustomException ex) {
        ResponseDTO<String> response = new ResponseDTO<>(
                RESOURCE_NOT_FOUND,
                ex.getMessage(),
                false
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}

