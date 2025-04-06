package com.example.clientservice.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Modelo que representa la respuesta de un servicio de autenticación.
 */
@Data
public class TokenResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private int expiresIn;
}