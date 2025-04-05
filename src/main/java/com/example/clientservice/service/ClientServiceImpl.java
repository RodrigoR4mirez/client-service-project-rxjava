package com.example.clientservice.service;

import com.example.clientservice.exception.ClientNotFoundException;
import com.example.clientservice.exception.ServiceException;
import com.example.clientservice.model.ClientRequest;
import com.example.clientservice.model.ClientResponse;
import com.example.clientservice.repository.ClientRepository;
import io.reactivex.rxjava3.core.Single;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import retrofit2.HttpException;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * Implementación de la interfaz ClientService que gestiona la lógica de negocio para clientes.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public Single<ClientResponse> getClientData(ClientRequest request) {
        log.info("Buscando datos para el cliente con documento: {}", request.getDocumentNumber());

        return clientRepository.getClientData(request)
                .doOnSuccess(response -> log.info("Cliente encontrado con ID: {}", response.getId()))
                .doOnError(error -> handleRequestError(error, request))
                .onErrorResumeNext(error -> {
                    if (error instanceof ClientNotFoundException) {
                        return Single.error(error);
                    }
                    if (error instanceof HttpException) {
                        HttpException httpError = (HttpException) error;
                        if (httpError.code() == HttpStatus.NOT_FOUND.value()) {
                            return Single.error(new ClientNotFoundException(
                                    "No se encontró un cliente con el documento: " + request.getDocumentNumber()));
                        }
                    }
                    return Single.error(new ServiceException("Error al obtener datos del cliente", error));
                });
    }

    /**
     * Maneja los errores específicos de las peticiones HTTP y registra información relevante.
     */
    private void handleRequestError(Throwable error, ClientRequest request) {
        if (error instanceof HttpException) {
            HttpException httpError = (HttpException) error;
            log.error("Error HTTP {} al buscar cliente con documento: {}",
                    httpError.code(), request.getDocumentNumber());
        } else if (error instanceof SocketTimeoutException) {
            log.error("Timeout al comunicarse con el servicio externo para el documento: {}",
                    request.getDocumentNumber());
        } else if (error instanceof IOException) {
            log.error("Error de conexión al buscar cliente con documento: {}",
                    request.getDocumentNumber());
        } else {
            log.error("Error inesperado al buscar cliente", error);
        }
    }
}