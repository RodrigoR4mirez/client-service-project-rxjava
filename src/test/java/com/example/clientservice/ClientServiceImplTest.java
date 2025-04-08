package com.example.clientservice;


import com.example.clientservice.mapper.ClientMapper;
import com.example.clientservice.model.ApiHeaders;
import com.example.clientservice.model.ClientRequest;
import com.example.clientservice.model.ClientResponse;
import com.example.clientservice.repository.ClientRepository;
import com.example.clientservice.repository.TokenRepository;
import com.example.clientservice.service.ClientServiceImpl;
import com.example.clientservice.service.TokenService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.*;

/**
 * Test directo del ClientServiceImpl que le permite ejecutar
 * su flujo completo sin modificaciones.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev") // Usar perfil de desarrollo para usar MockRepositories existentes
public class ClientServiceImplTest {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ClientMapper clientMapper;

    // El servicio que probaremos directamente
    private ClientServiceImpl clientServiceImpl;

    @Before
    public void setUp() {
        // Crear una instancia directa sin usar ningún mock
        clientServiceImpl = new ClientServiceImpl(
                clientRepository,
                tokenService,
                clientMapper
        );

        // Establecer los valores @Value manualmente usando ReflectionTestUtils
        ReflectionTestUtils.setField(clientServiceImpl, "applicationName", "client-service-direct-test");
        ReflectionTestUtils.setField(clientServiceImpl, "applicationVersion", "1.0.0");
    }

    @Test
    public void testDirectServiceCall() {
        // Preparamos los datos para llamar directamente al servicio
        String transactionId = "TX-DIRECT-" + System.currentTimeMillis();
        ClientRequest request = ClientRequest.builder()
                .documentNumber("12345678")
                .name("Juan Perez")
                .transactionId(transactionId)
                .build();

        // Creamos headers como lo haría un cliente real
        ApiHeaders headers = ApiHeaders.builder()
                .contentType("application/json")
                .accept("application/json")
                .channel("WEB")
                .transactionId(transactionId)
                .sourceSystem("test-system")
                .build();

        // Llamamos directamente al servicio y obtenemos respuesta síncrona
        ClientResponse response = clientServiceImpl.getClientData(transactionId, request, headers)
                .blockingGet(); // Espera y obtiene el resultado

        // Verificamos resultados
        assertNotNull("Debe retornar una respuesta", response);
        assertEquals("El ID debe tener el formato correcto", "CLI-" + request.getDocumentNumber(), response.getId());
        assertEquals("El documento debe coincidir", request.getDocumentNumber(), response.getDocumentNumber());
        assertEquals("El nombre debe coincidir", request.getName(), response.getName());
        assertNotNull("El correo no debe ser nulo", response.getEmail());
        assertNotNull("La dirección no debe ser nula", response.getAddress());
        assertEquals("El estado debe ser ACTIVE", "ACTIVE", response.getStatus());

        // Imprimimos la respuesta para verificación visual
        System.out.println("=== RESULTADO DE LA LLAMADA DIRECTA ===");
        System.out.println("Cliente: " + response);
        System.out.println("========================================");
    }
}