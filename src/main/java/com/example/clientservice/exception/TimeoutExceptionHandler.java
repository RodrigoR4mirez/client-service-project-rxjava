package com.example.clientservice.exception;

import java.io.IOException;

/**
 * Excepción personalizada para manejar timeouts de manera específica
 */
public class TimeoutExceptionHandler extends IOException {

    public TimeoutExceptionHandler(String message) {
        super(message);
    }

    public TimeoutExceptionHandler(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Método para obtener el código de error específico
     */
    public String getErrorCode() {
        return "TIMEOUT_ERROR_001";
    }

    /**
     * Método para determinar si es un error recuperable
     */
    public boolean isRecoverable() {
        return false; // En este caso, no es recuperable
    }
}