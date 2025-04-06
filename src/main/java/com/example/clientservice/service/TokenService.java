package com.example.clientservice.service;

import io.reactivex.rxjava3.core.Single;

/**
 * Interfaz que define los servicios para la gestión de tokens de autenticación.
 */
public interface TokenService {

    /**
     * Obtiene un token de autenticación del servicio externo.
     *
     * @return Un Single que emite el token de acceso como String
     */
    Single<String> getAuthToken();
}