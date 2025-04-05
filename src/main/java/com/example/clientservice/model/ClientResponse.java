package com.example.clientservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Representa la respuesta que contiene la información completa del cliente.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientResponse {

    private String id;
    private String documentType;
    private String documentNumber;
    private String name;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private LocalDate birthDate;
    private String status;
    private LocalDate registrationDate;

}