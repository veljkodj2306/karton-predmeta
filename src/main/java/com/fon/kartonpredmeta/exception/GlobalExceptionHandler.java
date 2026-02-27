package com.fon.kartonpredmeta.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NotFoundException ex, HttpServletRequest request) {

        ApiError body = new ApiError
                (LocalDateTime.now(), 404, "Not Found", ex.getMessage(), request.getRequestURI(), null);


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }


    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflict(ConflictException ex, HttpServletRequest request) {
        ApiError body = new ApiError(
                LocalDateTime.now(), 409, "Conflict", ex.getMessage(), request.getRequestURI(), null);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);

    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach((fieldError) -> fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage()));

        ApiError body = new ApiError(
                LocalDateTime.now(),
                400,
                "Bad Request",
                "Greska u validaciji",
                request.getRequestURI(),
                fieldErrors
        );


        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleBadJson(HttpMessageNotReadableException ex, HttpServletRequest request) {

        String message = "Neispravan JSON format.";

        if (ex.getMessage() != null && ex.getMessage().contains("TipPredmeta")) {
            message = "Neispravan tip predmeta, dozvoljene opcije: OBAVEZAN, IZBORNI";
        }

        ApiError body = new ApiError(
                LocalDateTime.now(),
                400,
                "Bad Request",
                message,
                request.getRequestURI(),
                null
        );


        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);

    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolation(DataIntegrityViolationException ex, HttpServletRequest request) {

        String message = "Konflikt podataka u bazi";

        if (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("sifra")) {
            message = "Predmet sa sifrom vec postoji";
        }

        ApiError body = new ApiError(
                LocalDateTime.now(),
                409,
                "Conflict",
                message,
                request.getRequestURI(),
                null
        );


        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }
}
