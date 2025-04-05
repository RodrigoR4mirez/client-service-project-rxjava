package com.example.clientservice.repository;

import com.example.clientservice.model.ClientRequest;
import com.example.clientservice.model.ClientResponse;
import io.reactivex.rxjava3.core.Single;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Implementación mock del repositorio de clientes para desarrollo y pruebas.
 * Esta clase simula las respuestas de una API externa.
 */
@Component
@Primary
@Profile("!prod")
@Slf4j
public class MockClientRepository implements ClientRepository {

    @Override
    public Single<ClientResponse> getClientData(ClientRequest request) {
        log.info("Mock: Procesando solicitud para cliente con documento: {}", request.getDocumentNumber());

        // Simulamos un pequeño retraso como lo haría una API real
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Si el documento es "99999999", simulamos un cliente no encontrado
        if ("99999999".equals(request.getDocumentNumber())) {
            return Single.error(new RuntimeException("Cliente no encontrado"));
        }

        // Para cualquier otro documento, devolvemos datos ficticios
        return Single.just(
                ClientResponse.builder()
                        .id("CLI-" + request.getDocumentNumber())
                        .documentType("DNI")
                        .documentNumber(request.getDocumentNumber())
                        .name(request.getName())
                        .lastName("Apellido de Prueba")
                        .email(request.getName().toLowerCase().replace(" ", ".") + "@ejemplo.com")
                        .phoneNumber("987" + request.getDocumentNumber().substring(0, 6))
                        .address("Av. Ejemplo 123, Lima")
                        .birthDate(LocalDate.of(1985, 5, 15))
                        .status("ACTIVE")
                        .registrationDate(LocalDate.now().minusYears(2))
                        .build()
        );
    }
}