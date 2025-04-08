package com.example.clientservice.repository;

import com.example.clientservice.model.external.ExternalClientRequest;
import com.example.clientservice.model.external.ExternalClientResponse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.Map;

/**
 * Interfaz para las llamadas a la API externa de clientes mediante Retrofit.
 */
public interface ClientRepository {
    /**
     * Obtiene los datos completos de un cliente.
     *
     * @param transactionId El ID de la transacción (path parameter)
     * @param headers Headers dinámicos como el token de autenticación
     * @param request El objeto que contiene los datos del cliente
     * @return Un Single que emite la respuesta con los datos del cliente
     */
    @POST("/api/v1/clients/{transactionId}/search")
    Single<ExternalClientResponse> getClientData(
            @Path("transactionId") String transactionId,
            @HeaderMap Map<String, String> headers,
            @Body ExternalClientRequest request
    );
}