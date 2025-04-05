package com.example.clientservice.exception;

/**
 * Excepción lanzada cuando no se encuentra un cliente con los datos proporcionados.
 */
public class ClientNotFoundException extends RuntimeException {

    public ClientNotFoundException(String message) {
        super(message);
    }

    public ClientNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}