package com.example.clientservice.util;

import com.example.clientservice.model.ApiHeaders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase utilitaria para el manejo de headers HTTP.
 */
@Component
@Slf4j
public class HeadersUtil {

    /**
     * Extrae los headers de una petición HTTP a un objeto ApiHeaders.
     *
     * @param request       La petición HTTP
     * @return Un objeto ApiHeaders con los datos extraídos
     */
    public ApiHeaders extractHeadersFromRequest(HttpServletRequest request) {
        Map<String, String> headersMap = extractHeadersToMap(request);
        ApiHeaders headers = mapToApiHeaders(headersMap);
        log.debug("Headers procesados: {}", headers);
        return headers;
    }

    /**
     * Extrae los headers de la petición HTTP a un Map.
     */
    private Map<String, String> extractHeadersToMap(HttpServletRequest request) {
        Map<String, String> headersMap = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();

        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                headersMap.put(headerName, headerValue);
            }
        }

        return headersMap;
    }

    /**
     * Convierte un Map de headers a un objeto ApiHeaders.
     */
    private ApiHeaders mapToApiHeaders(Map<String, String> headersMap) {
        ApiHeaders headers = new ApiHeaders();

        // Mapeo directo de nombres de headers HTTP a campos del objeto ApiHeaders
        if (headersMap.containsKey("Authorization")) {
            headers.setAuthorization(headersMap.get("Authorization"));
        }

        if (headersMap.containsKey("X-Correlation-ID")) {
            headers.setCorrelationId(headersMap.get("X-Correlation-ID"));
        }

        if (headersMap.containsKey("X-Channel")) {
            headers.setChannel(headersMap.get("X-Channel"));
        }

        if (headersMap.containsKey("X-Request-Timestamp")) {
            headers.setRequestTimestamp(headersMap.get("X-Request-Timestamp"));
        }

        if (headersMap.containsKey("X-Source-System")) {
            headers.setSourceSystem(headersMap.get("X-Source-System"));
        }

        if (headersMap.containsKey("X-Target-System")) {
            headers.setTargetSystem(headersMap.get("X-Target-System"));
        }

        if (headersMap.containsKey("Content-Type")) {
            headers.setContentType(headersMap.get("Content-Type"));
        }

        if (headersMap.containsKey("Accept")) {
            headers.setAccept(headersMap.get("Accept"));
        }

        if (headersMap.containsKey("User-Agent")) {
            headers.setUserAgent(headersMap.get("User-Agent"));
        }

        if (headersMap.containsKey("X-App-Version")) {
            headers.setAppVersion(headersMap.get("X-App-Version"));
        }

        return headers;
    }


}