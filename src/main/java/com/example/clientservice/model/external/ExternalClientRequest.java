package com.example.clientservice.model.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa la solicitud que se enviará a la API externa de clientes.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExternalClientRequest {
    private String documentNumber;
    private String name;
    private String transactionId;
    // Campos adicionales que pueda requerir la API externa
    private String channel;
    private String requestDate;
}