package org.cross.cauth.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationNotFoundException.class)
    public ResponseEntity<ErrorResponse> applicationNotFoundExceptionHandler(
            ApplicationNotFoundException ex,
            WebRequest request
    ){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .path(request.getDescription(false).replace("uri=", ""))
                .status(404)
                .timestamp(LocalDateTime.now())
                .error("Application Not Found")
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> invalidCredentialsExceptionHandler(
            InvalidCredentialsException ex,
            WebRequest request
    ){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .path(request.getDescription(false).replace("uri=", ""))
                .status(404)
                .timestamp(LocalDateTime.now())
                .error("Invalid Credentials")
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFoundExceptionHandler(
            UserNotFoundException ex,
            WebRequest request
    ){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .path(request.getDescription(false).replace("uri=", ""))
                .status(404)
                .timestamp(LocalDateTime.now())
                .error("User Not Found")
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidExceptionHandler(
            MethodArgumentNotValidException ex,
            WebRequest request
    ){
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(objectError -> errors.put(objectError.getField(), objectError.getDefaultMessage()));

        StringBuilder sb = new StringBuilder();
        errors.forEach((field, message) -> sb.append(field).append(": ").append(message).append("; "));

        ErrorResponse errorResponse = ErrorResponse.builder()
                .path(request.getDescription(false).replace("uri=", ""))
                .status(400)
                .timestamp(LocalDateTime.now())
                .error("Validation Failed")
                .message("Invalid input parameters: " + sb.toString())
                .build();

        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApplicationAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> applicationAlreadyExistsExceptionHandler(
            ApplicationAlreadyExistsException ex,
            WebRequest request
    ){

        ErrorResponse errorResponse = ErrorResponse.builder()
                .path(request.getDescription(false).replace("uri=", ""))
                .status(400)
                .timestamp(LocalDateTime.now())
                .error("Application already exists")
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> userAlreadyExistsExceptionHandler(
            UserAlreadyExistsException ex,
            WebRequest request
    ){

        ErrorResponse errorResponse = ErrorResponse.builder()
                .path(request.getDescription(false).replace("uri=", ""))
                .status(400)
                .timestamp(LocalDateTime.now())
                .error("User already exists")
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(
            Exception ex,
            WebRequest request
    ){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .path(request.getDescription(false).replace("uri=", ""))
                .status(500)
                .timestamp(LocalDateTime.now())
                .error("Internal Server Error")
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
