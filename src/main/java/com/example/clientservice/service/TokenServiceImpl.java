package com.example.clientservice.service;

import com.example.clientservice.model.TokenResponse;
import com.example.clientservice.repository.TokenRepository;
import io.reactivex.rxjava3.core.Single;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Implementación del servicio para la gestión de tokens de autenticación.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    @Value("${external.auth.client-id}")
    private String clientId;

    @Value("${external.auth.client-secret}")
    private String clientSecret;

    @Value("${external.auth.url:/auth/token}")
    private String tokenEndpoint;

    @Override
    public Single<String> getAuthToken() {
        log.info("Obteniendo token de autenticación en endpoint: {}", tokenEndpoint);

        return tokenRepository.getToken(tokenEndpoint, "client_credentials", clientId, clientSecret)
                .map(TokenResponse::getAccessToken)
                .doOnSuccess(token -> log.debug("Token obtenido exitosamente"))
                .doOnError(error -> log.error("Error al obtener token: {}", error.getMessage()));
    }
}