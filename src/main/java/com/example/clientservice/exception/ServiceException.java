package com.example.clientservice.exception;

/**
 * Excepción genérica para errores en el servicio.
 */
public class ServiceException extends RuntimeException {

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}