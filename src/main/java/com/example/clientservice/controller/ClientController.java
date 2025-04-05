package com.example.clientservice.controller;

import com.example.clientservice.model.ClientRequest;
import com.example.clientservice.model.ClientResponse;
import com.example.clientservice.service.ClientService;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * Endpoint que recibe una solicitud con información básica del cliente y devuelve sus datos completos.
     *
     * @param request La solicitud con documentNumber y name
     * @return ResponseEntity con los datos completos del cliente
     */
    @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public Single<ResponseEntity<ClientResponse>> searchClient(@Valid @RequestBody ClientRequest request) {
        log.info("Recibida solicitud de búsqueda para cliente con documento: {}", request.getDocumentNumber());

        return clientService.getClientData(request)
                .subscribeOn(Schedulers.io())
                .map(clientResponse -> {
                    log.info("Retornando datos para cliente ID: {}", clientResponse.getId());
                    return ResponseEntity.ok(clientResponse);
                });
    }
}