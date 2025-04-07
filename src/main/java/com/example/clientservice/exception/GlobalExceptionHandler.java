package com.example.clientservice.exception;

import com.example.clientservice.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;

/**
 * Manejador global de excepciones para la API.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Maneja las excepciones de validación de campos.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {

        ErrorResponse errorResponse = buildErrorResponse(
                ApiError.VALIDATION_ERROR,
                request.getDescription(false));

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errorResponse.addError(fieldName + ": " + errorMessage);
        });

        log.error("Validation error: {}", errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja las excepciones de cliente no encontrado.
     */
    @ExceptionHandler(ClientNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleClientNotFoundException(
            ClientNotFoundException ex, WebRequest request) {

        ErrorResponse errorResponse = buildErrorResponse(
                ApiError.CLIENT_NOT_FOUND,
                request.getDescription(false));

        errorResponse.addError(ex.getMessage());

        log.error("Client not found: {}", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

//    /**
//     * Maneja las excepciones de timeout.
//     */
//    @ExceptionHandler({TimeoutException.class, SocketTimeoutException.class})
//    @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
//    public ResponseEntity<ErrorResponse> handleTimeoutException(
//            Exception ex, WebRequest request) {
//
//        ErrorResponse errorResponse = buildErrorResponse(
//                ApiError.TIMEOUT_ERROR,
//                request.getDescription(false));
//
//        errorResponse.addError("Timeout al comunicarse con el servicio externo: " + ex.getMessage());
//
//        log.error("Timeout error: {}", ex.getMessage());
//        return new ResponseEntity<>(errorResponse, HttpStatus.GATEWAY_TIMEOUT);
//    }

    /**
     * Maneja las excepciones del servicio.
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<ErrorResponse> handleServiceException(
            ServiceException ex, WebRequest request) {

        ErrorResponse errorResponse = buildErrorResponse(
                ApiError.SERVICE_UNAVAILABLE,
                request.getDescription(false));

        errorResponse.addError(ex.getMessage());

        log.error("Service error: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

    /**
     * Maneja cualquier excepción no controlada.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {

        ErrorResponse errorResponse = buildErrorResponse(
                ApiError.INTERNAL_SERVER_ERROR,
                request.getDescription(false));

        errorResponse.addError("Se ha producido un error inesperado: " + ex.getMessage());

        log.error("Unexpected error: ", ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Construye un objeto ErrorResponse a partir de un ApiError.
     */
    private ErrorResponse buildErrorResponse(ApiError apiError, String path) {
        return ErrorResponse.builder()
                .code(apiError.getCode())
                .status(apiError.getStatus().toString())
                .message(apiError.getMessage())
                .timestamp(LocalDateTime.now())
                .path(path)
                .build();
    }

    @ExceptionHandler({
            TimeoutException.class,
            SocketTimeoutException.class,
            TimeoutExceptionHandler.class
    })
    @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
    public ResponseEntity<ErrorResponse> handleTimeoutException(
            Exception ex, WebRequest request) {

        String errorMessage = "Timeout al comunicarse con el servicio externo";
        String errorCode = "TIMEOUT_ERROR_001";

        if (ex instanceof TimeoutExceptionHandler) {
            TimeoutExceptionHandler timeoutEx = (TimeoutExceptionHandler) ex;
            errorCode = timeoutEx.getErrorCode();
            errorMessage += ": " + timeoutEx.getMessage();
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(errorCode)
                .status(HttpStatus.GATEWAY_TIMEOUT.toString())
                .message("Timeout en servicio externo")
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false))
                .build();

        errorResponse.addError(errorMessage);

        log.error("Timeout error: {}", errorMessage, ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.GATEWAY_TIMEOUT);
    }
}