package com.example.clientservice.mapper;

import com.example.clientservice.model.ClientRequest;
import com.example.clientservice.model.ClientResponse;
import com.example.clientservice.model.external.ExternalClientRequest;
import com.example.clientservice.model.external.ExternalClientResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Interfaz para mapear entre los modelos internos y externos relacionados con clientes.
 */
@Mapper(componentModel = "spring", imports = {LocalDateTime.class, DateTimeFormatter.class})
public interface ClientMapper {

    /**
     * Convierte un objeto ClientRequest (modelo interno) a ExternalClientRequest (modelo para API externa).
     */
    @Mapping(target = "channel", constant = "WEB")
    @Mapping(target = "requestDate", expression = "java(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))")
    ExternalClientRequest toExternalRequest(ClientRequest request, String transactionId);

    /**
     * Convierte un objeto ExternalClientResponse (respuesta de API externa) a ClientResponse (modelo interno).
     */
    @Mapping(source = "clientId", target = "id")
    @Mapping(source = "firstName", target = "name")
    @Mapping(source = "emailAddress", target = "email")
    @Mapping(source = "residentialAddress", target = "address")
    @Mapping(source = "dateOfBirth", target = "birthDate")
    @Mapping(source = "clientStatus", target = "status")
    ClientResponse toClientResponse(ExternalClientResponse externalResponse);
}