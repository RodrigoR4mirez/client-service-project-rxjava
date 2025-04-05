package com.example.clientservice.repository;

import com.example.clientservice.model.ClientRequest;
import com.example.clientservice.model.ClientResponse;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Interfaz para las llamadas a la API externa de clientes mediante Retrofit.
 */
public interface ClientRepository {

    /**
     * Obtiene los datos completos de un cliente basado en su número de documento y nombre.
     *
     * @param request El objeto que contiene el número de documento y nombre del cliente
     * @return Un Single que emite la respuesta con los datos del cliente
     */
    @POST("/api/v1/clients/search")
    Single<ClientResponse> getClientData(@Body ClientRequest request);
}