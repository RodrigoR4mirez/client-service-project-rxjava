package com.example.clientservice.util;

import com.example.clientservice.model.ApiHeaders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class HeadersUtil {

    /**
     * Extrae los headers de una petición HTTP a un objeto ApiHeaders.
     *
     * @param request La petición HTTP
     * @return Un objeto ApiHeaders con los datos extraídos
     */
    public ApiHeaders extractHeadersFromRequest(HttpServletRequest request) {
        Map<String, String> headersMap = extractHeadersToMap(request);
        ApiHeaders headers = mapToApiHeaders(headersMap);
        log.debug("Headers procesados: {}", headers);
        return headers;
    }

    /**
     * Extrae los headers de la petición HTTP a un Map, con soporte para headers personalizados.
     */
    private Map<String, String> extractHeadersToMap(HttpServletRequest request) {
        Map<String, String> headersMap = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();

        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);

                // Normalizar nombres de headers
                String normalizedName = normalizeHeaderName(headerName);

                // Solo agregar headers con valor no vacío
                if (StringUtils.hasText(headerValue)) {
                    headersMap.put(normalizedName, headerValue);
                }
            }
        }

        return headersMap;
    }

    /**
     * Normaliza los nombres de headers para mapeo consistente.
     */
    private String normalizeHeaderName(String headerName) {
        // Convertir a minúsculas para comparación case-insensitive
        headerName = headerName.toLowerCase();

        // Mapeo de headers personalizados
        switch (headerName) {
            case "authorization":
                return "Authorization";
            case "x-transaction-id":
                return "X-Transaction-ID";
            case "x-correlation-id":
                return "X-Correlation-ID";
            case "x-channel":
                return "X-Channel";
            case "x-request-timestamp":
                return "X-Request-Timestamp";
            case "x-source-system":
                return "X-Source-System";
            case "x-target-system":
                return "X-Target-System";
            case "content-type":
                return "Content-Type";
            case "accept":
                return "Accept";
            case "user-agent":
                return "User-Agent";
            case "x-app-version":
                return "X-App-Version";
            default:
                return headerName;
        }
    }

    /**
     * Convierte un Map de headers a un objeto ApiHeaders con mapeo más robusto.
     */
    private ApiHeaders mapToApiHeaders(Map<String, String> headersMap) {
        ApiHeaders.ApiHeadersBuilder builder = ApiHeaders.builder();

        // Mapeo explícito de headers conocidos
        if (headersMap.containsKey("Authorization")) {
            builder.authorization(headersMap.get("Authorization"));
        }

        if (headersMap.containsKey("X-Transaction-ID")) {
            builder.transactionId(headersMap.get("X-Transaction-ID"));
        }

        if (headersMap.containsKey("X-Correlation-ID")) {
            builder.correlationId(headersMap.get("X-Correlation-ID"));
        }

        if (headersMap.containsKey("X-Channel")) {
            builder.channel(headersMap.get("X-Channel"));
        }

        if (headersMap.containsKey("X-Request-Timestamp")) {
            builder.requestTimestamp(headersMap.get("X-Request-Timestamp"));
        }

        if (headersMap.containsKey("X-Source-System")) {
            builder.sourceSystem(headersMap.get("X-Source-System"));
        }

        if (headersMap.containsKey("X-Target-System")) {
            builder.targetSystem(headersMap.get("X-Target-System"));
        }

        if (headersMap.containsKey("Content-Type")) {
            builder.contentType(headersMap.get("Content-Type"));
        }

        if (headersMap.containsKey("Accept")) {
            builder.accept(headersMap.get("Accept"));
        }

        if (headersMap.containsKey("User-Agent")) {
            builder.userAgent(headersMap.get("User-Agent"));
        }

        if (headersMap.containsKey("X-App-Version")) {
            builder.appVersion(headersMap.get("X-App-Version"));
        }

        return builder.build();
    }
}