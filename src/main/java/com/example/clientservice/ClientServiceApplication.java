package com.example.clientservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Clase principal que inicia la aplicación Spring Boot.
 */
@SpringBootApplication
@Slf4j
public class ClientServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientServiceApplication.class, args);
    }
}
//    /**
//     * Método principal que inicia la aplicación.
//     *
//     * @param args Argumentos de línea de comandos
//     * @throws UnknownHostException Si no se puede determinar la dirección del host
//     */
//    public static void main(String[] args) throws UnknownHostException {
//        ApplicationContext ctx = SpringApplication.run(ClientServiceApplication.class, args);
//        Environment env = ctx.getEnvironment();
//
//        String protocol = "http";
//        if (env.getProperty("server.ssl.key-store") != null) {
//            protocol = "https";
//        }
//
//        String hostAddress = InetAddress.getLocalHost().getHostAddress();
//        String port = env.getProperty("server.port", "8080");
//        String contextPath = env.getProperty("server.servlet.context-path", "/");
//
//        if (contextPath.isEmpty()) {
//            contextPath = "/";
//        }
//
//        log.info("\n----------------------------------------------------------\n\t" +
//                        "Aplicación '{}' está ejecutándose!\n\t" +
//                        "Acceso local: \t\t{}://localhost:{}{}\n\t" +
//                        "Acceso externo: \t{}://{}:{}{}\n\t" +
//                        "Perfil(es): \t\t{}\n" +
//                        "----------------------------------------------------------",
//                env.getProperty("spring.application.name", "Client Service"),
//                protocol,
//                port,
//                contextPath,
//                protocol,
//                hostAddress,
//                port,
//                contextPath,
//                env.getActiveProfiles().length == 0 ? "default" : String.join(", ", env.getActiveProfiles()));
//    }
//}