package com.m2code.exception;

import com.m2code.dtos.ApiResponseMessage;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    protected ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex) {
        String message = ex.getMessage();
        ApiResponseMessage<String> apiResponseMessage = ApiResponseMessage.
                <String>builder().message(message).success(false).build();
        return new ResponseEntity<>(apiResponseMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadApiRequestException.class)
    protected ResponseEntity<Object> handleBadApiRequest(BadApiRequestException ex) {
        String message = ex.getMessage();
        ApiResponseMessage<String> apiResponseMessage = ApiResponseMessage.
                <String>builder().message(message).success(false).build();
        return new ResponseEntity<>(apiResponseMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JwtException.class)
    protected ResponseEntity<Object> handleJwtException(JwtException ex) {
        String message = ex.getMessage();
        ApiResponseMessage<String> apiResponseMessage = ApiResponseMessage.
                <String>builder().message(message).success(false).build();
        return new ResponseEntity<>(apiResponseMessage, HttpStatus.UNAUTHORIZED);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errorMessage = ex.getBindingResult().getAllErrors().stream().filter(error -> error instanceof FieldError).collect(Collectors.toMap(error -> ((FieldError) error).getField(), error -> error.getDefaultMessage()));
        ApiResponseMessage<Map<String, String>> apiResponseMessage = ApiResponseMessage
                .<Map<String, String>>builder().message(errorMessage).success(false)
                .build();
        return new ResponseEntity<>(apiResponseMessage, HttpStatus.BAD_REQUEST);
    }
}
