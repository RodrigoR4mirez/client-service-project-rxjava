package com.example.clientservice.model.external;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Representa la respuesta que se recibe de la API externa de clientes.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExternalClientResponse {
    private String clientId;
    private String documentType;
    private String documentNumber;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
    private String residentialAddress;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private String clientStatus;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate registrationDate;

    private boolean hasPendingProcesses;
    private int riskCategory;
}