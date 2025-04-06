package com.example.clientservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Modelo que representa los headers HTTP para las APIs.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiHeaders {

    private String authorization;
    private String transactionId;
    private String correlationId;
    private String channel;
    private String requestTimestamp;
    private String sourceSystem;
    private String targetSystem;
    private String contentType;
    private String accept;
    private String userAgent;
    private String appVersion;

    /**
     * Convierte los headers a un Map para uso con Retrofit.
     * Solo incluye los campos que no son nulos o vacíos.
     */
    public Map<String, String> toMap() {
        Map<String, String> headerMap = new HashMap<>();

        if (StringUtils.hasText(authorization)) {
            headerMap.put("Authorization", authorization);
        }

        if (StringUtils.hasText(transactionId)) {
            headerMap.put("X-Transaction-ID", transactionId);
        }

        if (StringUtils.hasText(correlationId)) {
            headerMap.put("X-Correlation-ID", correlationId);
        }

        if (StringUtils.hasText(channel)) {
            headerMap.put("X-Channel", channel);
        }

        if (StringUtils.hasText(requestTimestamp)) {
            headerMap.put("X-Request-Timestamp", requestTimestamp);
        }

        if (StringUtils.hasText(sourceSystem)) {
            headerMap.put("X-Source-System", sourceSystem);
        }

        if (StringUtils.hasText(targetSystem)) {
            headerMap.put("X-Target-System", targetSystem);
        }

        if (StringUtils.hasText(contentType)) {
            headerMap.put("Content-Type", contentType);
        }

        if (StringUtils.hasText(accept)) {
            headerMap.put("Accept", accept);
        }

        if (StringUtils.hasText(userAgent)) {
            headerMap.put("User-Agent", userAgent);
        }

        if (StringUtils.hasText(appVersion)) {
            headerMap.put("X-App-Version", appVersion);
        }

        return headerMap;
    }

    /**
     * Crea un objeto ApiHeaders a partir de un Map de headers HTTP.
     */
    public static ApiHeaders fromMap(Map<String, String> headerMap) {
        ApiHeadersBuilder builder = ApiHeaders.builder();

        if (headerMap.containsKey("Authorization")) {
            builder.authorization(headerMap.get("Authorization"));
        }

        if (headerMap.containsKey("X-Transaction-ID")) {
            builder.transactionId(headerMap.get("X-Transaction-ID"));
        }

        if (headerMap.containsKey("X-Correlation-ID")) {
            builder.correlationId(headerMap.get("X-Correlation-ID"));
        }

        if (headerMap.containsKey("X-Channel")) {
            builder.channel(headerMap.get("X-Channel"));
        }

        if (headerMap.containsKey("X-Request-Timestamp")) {
            builder.requestTimestamp(headerMap.get("X-Request-Timestamp"));
        }

        if (headerMap.containsKey("X-Source-System")) {
            builder.sourceSystem(headerMap.get("X-Source-System"));
        }

        if (headerMap.containsKey("X-Target-System")) {
            builder.targetSystem(headerMap.get("X-Target-System"));
        }

        if (headerMap.containsKey("Content-Type")) {
            builder.contentType(headerMap.get("Content-Type"));
        }

        if (headerMap.containsKey("Accept")) {
            builder.accept(headerMap.get("Accept"));
        }

        if (headerMap.containsKey("User-Agent")) {
            builder.userAgent(headerMap.get("User-Agent"));
        }

        if (headerMap.containsKey("X-App-Version")) {
            builder.appVersion(headerMap.get("X-App-Version"));
        }

        return builder.build();
    }

    /**
     * Crea headers por defecto.
     */
    public static ApiHeaders createDefault() {
        return ApiHeaders.builder()
                .contentType("application/json")
                .accept("application/json")
                .appVersion("1.0.0")
                .sourceSystem("client-service")
                .build();
    }
}