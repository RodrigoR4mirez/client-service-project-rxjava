package com.example.clientservice.controller;

import com.example.clientservice.model.ApiHeaders;
import com.example.clientservice.model.ClientRequest;
import com.example.clientservice.model.ClientResponse;
import com.example.clientservice.service.ClientService;
import com.example.clientservice.util.HeadersUtil;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Controlador REST que expone los endpoints para gestión de clientes.
 */
@RestController
@RequestMapping("/api/clients")
@Slf4j
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final HeadersUtil headersUtil;

    /**
     * Endpoint que recibe una solicitud con información básica del cliente y devuelve sus datos completos.
     *
     * @param transactionId El ID de transacción único para esta operación
     * @param request La solicitud con documentNumber y name
     * @param httpRequest La solicitud HTTP para extraer los headers
     * @return ResponseEntity con los datos completos del cliente
     */
    @PostMapping(value = "/{transactionId}/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public Single<ResponseEntity<ClientResponse>> searchClient(
            @PathVariable String transactionId,
            @Valid @RequestBody ClientRequest request,
            HttpServletRequest httpRequest) {

        log.info("Recibida solicitud de búsqueda para cliente con documento: {}, transactionId: {}",
                request.getDocumentNumber(), transactionId);

        // Extraer directamente los headers HTTP a un objeto ApiHeaders y establecer transactionId
        ApiHeaders headers = headersUtil.extractHeadersFromRequest(httpRequest);

        return clientService.getClientData(transactionId, request, headers)
                .subscribeOn(Schedulers.io())
                .map(clientResponse -> {
                    log.info("Retornando datos para cliente ID: {}", clientResponse.getId());
                    return ResponseEntity.ok(clientResponse);
                });
    }
}