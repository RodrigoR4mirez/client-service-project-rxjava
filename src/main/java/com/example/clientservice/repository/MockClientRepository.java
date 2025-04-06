package com.example.clientservice.repository;

import com.example.clientservice.model.external.ExternalClientRequest;
import com.example.clientservice.model.external.ExternalClientResponse;
import io.reactivex.rxjava3.core.Single;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;

/**
 * Implementación mock del repositorio de clientes para desarrollo y pruebas.
 */
@Component
@Primary
@Profile("!prod")
@Slf4j
public class MockClientRepository implements ClientRepository {

    @Override
    public Single<ExternalClientResponse> getClientData(
            String transactionId,
            Map<String, String> headers,
            ExternalClientRequest request) {

        log.info("Mock: Procesando solicitud para cliente con documento: {}, transactionId: {}",  request.getDocumentNumber(), transactionId);
        log.info("Mock: Headers recibidos: {}", headers);

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

        // Para cualquier otro documento, devolvemos datos ficticios en formato de API externa
        return Single.just(
                ExternalClientResponse.builder()
                        .clientId("CLI-" + request.getDocumentNumber())
                        .documentType("DNI")
                        .documentNumber(request.getDocumentNumber())
                        .firstName(request.getName())
                        .lastName("Apellido de Prueba")
                        .emailAddress(request.getName().toLowerCase().replace(" ", ".") + "@ejemplo.com")
                        .phoneNumber("987" + request.getDocumentNumber().substring(0, 6))
                        .residentialAddress("Av. Ejemplo 123, Lima")
                        .dateOfBirth(LocalDate.of(1985, 5, 15))
                        .clientStatus("ACTIVE")
                        .registrationDate(LocalDate.now().minusYears(2))
                        .hasPendingProcesses(false)
                        .riskCategory(1)
                        .build()
        );
    }
}