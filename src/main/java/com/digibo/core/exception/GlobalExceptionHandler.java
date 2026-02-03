package com.digibo.core.exception;

import com.digibo.core.dto.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Value("${spring.profiles.active:prod}")
    private String activeProfile;

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<ErrorResponse> handleDatabaseException(DatabaseException ex) {
        logger.error("Database error in {}.{}: {}", ex.getPackageName(), ex.getProcedureName(), ex.getMessage(), ex);

        ErrorResponse response = new ErrorResponse();
        response.setError("Database Error");
        response.setMessage("A database error occurred");
        response.setCode(ex.getErrorCode());

        if (isDevelopment()) {
            response.setDetails(ex.getMessage());
            response.setPackageName(ex.getPackageName());
            response.setProcedure(ex.getProcedureName());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErrorResponse> handleSQLException(SQLException ex) {
        logger.error("SQL error: {}", ex.getMessage(), ex);

        ErrorResponse response = new ErrorResponse();
        String errorCode = String.valueOf(ex.getErrorCode());

        if (errorCode.startsWith("6550")) {
            response.setError("Database Procedure Error");
            response.setMessage("The requested database procedure could not be executed");
        } else {
            response.setError("Database Error");
            response.setMessage("A database error occurred");
        }
        response.setCode(errorCode);

        if (isDevelopment()) {
            response.setDetails(ex.getMessage());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException ex) {
        logger.error("Data access error: {}", ex.getMessage(), ex);

        ErrorResponse response = new ErrorResponse();
        response.setError("Database Error");
        response.setMessage("A database error occurred");

        if (isDevelopment()) {
            response.setDetails(ex.getMessage());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
        logger.warn("Validation error: {}", ex.getMessage());

        ErrorResponse response = new ErrorResponse();
        response.setError("Validation Error");
        response.setMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponse response = new ErrorResponse();
        response.setError("Validation Error");
        response.setMessage("Request validation failed");
        response.setFieldErrors(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse response = new ErrorResponse();
        response.setError("Not Found");
        response.setMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        ErrorResponse response = new ErrorResponse();
        response.setError("Not Found");
        response.setMessage(String.format("Route %s %s not found", ex.getHttpMethod(), ex.getRequestURL()));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {
        ErrorResponse response = new ErrorResponse();
        response.setError("Unauthorized");
        response.setMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        logger.error("Unexpected error: {}", ex.getMessage(), ex);

        ErrorResponse response = new ErrorResponse();
        response.setError("Internal Server Error");
        response.setMessage("An unexpected error occurred");

        if (isDevelopment()) {
            response.setDetails(ex.getMessage());
            response.setStackTrace(getStackTraceString(ex));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private boolean isDevelopment() {
        return "dev".equals(activeProfile) || "mock".equals(activeProfile);
    }

    private String getStackTraceString(Exception ex) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : ex.getStackTrace()) {
            sb.append(element.toString()).append("\n");
            if (sb.length() > 2000) {
                sb.append("...");
                break;
            }
        }
        return sb.toString();
    }
}
