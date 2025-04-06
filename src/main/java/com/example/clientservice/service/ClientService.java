package com.example.clientservice.service;

import com.example.clientservice.model.ApiHeaders;
import com.example.clientservice.model.ClientRequest;
import com.example.clientservice.model.ClientResponse;
import io.reactivex.rxjava3.core.Single;

/**
 * Interfaz que define los servicios para la gestión de clientes.
 */
public interface ClientService {

    /**
     * Obtiene los datos completos de un cliente basado en su información básica.
     *
     * @param transactionId El ID de la transacción
     * @param request Objeto con la información básica del cliente
     * @param headers Headers de la petición original
     * @return Un Single que emite la respuesta con los datos completos del cliente
     */
    Single<ClientResponse> getClientData(String transactionId, ClientRequest request, ApiHeaders headers);
}