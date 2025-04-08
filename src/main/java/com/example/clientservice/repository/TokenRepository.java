package com.example.clientservice.repository;

import com.example.clientservice.model.TokenResponse;
import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Interfaz para las llamadas al API de autenticación.
 */
public interface TokenRepository {

    /**
     * Obtiene un token de autenticación usando credenciales.
     *
     * @param url Endpoint de la API de autenticación
     * @param grantType Tipo de concesión (usualmente "client_credentials")
     * @param clientId ID del cliente
     * @param clientSecret Secreto del cliente
     * @return Un Single que emite la respuesta con el token
     */
    @FormUrlEncoded
    @POST
    Single<TokenResponse> getToken(
            @Url String url,
            @Field("grant_type") String grantType,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret
    );
}