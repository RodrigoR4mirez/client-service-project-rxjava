package com.example.clientservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Enum que define los posibles errores de la API con su código y estado HTTP.
 */
@Getter
@AllArgsConstructor
public enum ApiError {

    VALIDATION_ERROR("ERR-001", HttpStatus.BAD_REQUEST, "Error de validación"),
    CLIENT_NOT_FOUND("ERR-002", HttpStatus.NOT_FOUND, "Cliente no encontrado"),
    SERVICE_UNAVAILABLE("ERR-003", HttpStatus.SERVICE_UNAVAILABLE, "Servicio externo no disponible"),
    INTERNAL_SERVER_ERROR("ERR-004", HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor"),
    TIMEOUT_ERROR("ERR-005", HttpStatus.GATEWAY_TIMEOUT, "Timeout en la comunicación con servicio externo");

    private final String code;
    private final HttpStatus status;
    private final String message;
}