package com.example.clientservice.service;

import com.example.clientservice.exception.ClientNotFoundException;
import com.example.clientservice.exception.ServiceException;
import com.example.clientservice.mapper.ClientMapper;
import com.example.clientservice.model.ApiHeaders;
import com.example.clientservice.model.ClientRequest;
import com.example.clientservice.model.ClientResponse;
import com.example.clientservice.model.external.ExternalClientRequest;
import com.example.clientservice.repository.ClientRepository;
import io.reactivex.Single;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import retrofit2.HttpException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Implementación de la interfaz ClientService que gestiona la lógica de negocio para clientes.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final TokenService tokenService;
    private final ClientMapper clientMapper;

    @Value("${spring.application.name:client-service}")
    private String applicationName;

    @Value("${application.version:1.0.0}")
    private String applicationVersion;

    @Override
    public Single<ClientResponse> getClientData(String transactionId, ClientRequest request, ApiHeaders headers) {
        log.info("Buscando datos para el cliente con documento: {}, transactionId: {}",
                request.getDocumentNumber(), transactionId);

        // Convertimos el request interno al formato esperado por la API externa
        ExternalClientRequest externalRequest = clientMapper.toExternalRequest(request, transactionId);

        // Preparamos los headers y hacemos la llamada al API externo
        return prepareApiHeaders(headers, transactionId)
                .flatMap(headerMap -> clientRepository.getClientData(transactionId, headerMap, externalRequest))
                .map(clientMapper::toClientResponse)
                .doOnSuccess(response -> log.info("Cliente encontrado con ID: {}", response.getId()))
                .doOnError(error -> handleRequestError(error, request))
                .onErrorResumeNext(this::handleSpecificErrors);
    }

    /**
     * Prepara los headers para la API externa, completando la información faltante
     * y obteniendo el token de autenticación.
     *
     * @param headers Headers base recibidos del controlador
     * @param transactionId ID de transacción
     * @return Un Single que emite el Map de headers listo para enviar
     */
    private Single<Map<String, String>> prepareApiHeaders(ApiHeaders headers, String transactionId) {
        // Completamos los headers con información que falta
        completeMissingHeaderInfo(headers, transactionId);

        // Obtenemos el token y lo añadimos a los headers
        return tokenService.getAuthToken()
                .map(token -> {
                    headers.setAuthorization("Bearer " + token);
                    log.debug("Headers preparados para API externa: {}", headers);
                    return headers.toMap();
                });
    }

    /**
     * Completa la información faltante en los headers con valores por defecto.
     *
     * @param headers Headers a completar
     * @param transactionId ID de transacción
     */
    private void completeMissingHeaderInfo(ApiHeaders headers, String transactionId) {
        if (headers.getSourceSystem() == null) {
            headers.setSourceSystem(applicationName);
        }

        if (headers.getAppVersion() == null) {
            headers.setAppVersion(applicationVersion);
        }

        if (headers.getContentType() == null) {
            headers.setContentType("application/json");
        }

        if (headers.getAccept() == null) {
            headers.setAccept("application/json");
        }

        // Aseguramos que el transactionId de los headers coincida con el del path
        headers.setTransactionId(transactionId);

        // Aseguramos que hay un timestamp
        if (headers.getRequestTimestamp() == null) {
            headers.setRequestTimestamp(
                    LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        }

        // Establecer canal por defecto si no existe
        if (headers.getChannel() == null) {
            headers.setChannel("WEB");
        }
    }

    private Single<ClientResponse> handleSpecificErrors(Throwable error) {
        if (error instanceof ClientNotFoundException) {
            return Single.error(error);
        }
        if (error instanceof HttpException) {
            HttpException httpError = (HttpException) error;
            if (httpError.code() == HttpStatus.NOT_FOUND.value()) {
                return Single.error(new ClientNotFoundException(
                        "No se encontró el cliente solicitado"));
            }
        }
        return Single.error(new ServiceException("Error al obtener datos del cliente", error));
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