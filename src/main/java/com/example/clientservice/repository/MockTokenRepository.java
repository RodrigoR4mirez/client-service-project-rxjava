package com.example.clientservice.repository;

import com.example.clientservice.model.TokenResponse;
import io.reactivex.Single;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Implementación mock del repositorio de tokens para desarrollo y pruebas.
 */
@Component
@Primary
@Profile("!prod")
@Slf4j
public class MockTokenRepository implements TokenRepository {

    @Override
    public Single<TokenResponse> getToken(
            String url,
            String grantType,
            String clientId,
            String clientSecret) {

        log.info("Mock: Generando token para clientId: {} usando endpoint: {}", clientId, url);

        // Simulamos un pequeño retraso
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Generamos un token ficticio
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken("mock-token-" + System.currentTimeMillis());
        tokenResponse.setTokenType("Bearer");
        tokenResponse.setExpiresIn(3600);

        return Single.just(tokenResponse);
    }
}