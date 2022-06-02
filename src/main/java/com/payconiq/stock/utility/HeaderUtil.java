package com.payconiq.stock.utility;


/**
 * @author Milad Ranjbari
 * @version 2022.6.1
 * @since 6/01/22
 * Extracted from JHipster
 */


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public final class HeaderUtil {

    private HeaderUtil() {
    }

    public static String getRequestClientIpComplete(HttpServletRequest req) {
        String clientIp = req.getHeader("X-Real-IP");
        if (clientIp == null || "".equals(clientIp)) { // extract from forward ips
            String ipForwarded = req.getHeader("X-FORWARDED-FOR");
            String[] ips = ipForwarded == null ? null : ipForwarded.split(",");
            clientIp = (ips == null || ips.length == 0) ? null : ips[0];
            // extract from remote addr
            clientIp = (clientIp == null || clientIp.isEmpty()) ? req.getRemoteAddr() : clientIp;
        }
        if("0:0:0:0:0:0:0:1".equals(clientIp)) clientIp="127.0.0.1";
        return clientIp;
    }

    public static Map<String, String> getHeadersInfo(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

    public static Map<String, String> getHeadersInfo(HttpServletResponse response) {
        Map<String, String> map = new HashMap<>();
        if (null != response) {
            map = response.getHeaderNames()
                    .stream()
                    .collect(Collectors.toMap(
                            Function.identity(),
                            response::getHeader
                    ));
        }
        return map;
    }

    public static HttpHeaders createAlert(String applicationName, String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + applicationName + "-alert", message);

        try {
            headers.add("X-" + applicationName + "-params", URLEncoder.encode(param, StandardCharsets.UTF_8.toString()));
        } catch (UnsupportedEncodingException ignored) {
        }

        return headers;
    }

    public static HttpHeaders createEntityCreationAlert(String applicationName, boolean enableTranslation, String
            entityName, String param) {
        String message = enableTranslation ? applicationName + "." + entityName + ".created" : "A new " + entityName + " is created with identifier " + param;
        return createAlert(applicationName, message, param);
    }

    public static HttpHeaders createFailureAlert(String applicationName, boolean enableTranslation, String
            entityName, String errorKey, String defaultMessage) {
        log.error("Entity processing failed, {}", defaultMessage);
        String message = enableTranslation ? "error." + errorKey : defaultMessage;
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + applicationName + "-error", message);
        headers.add("X-" + applicationName + "-params", entityName);
        return headers;
    }

    public static HttpHeaders createEntityUpdateAlert(String applicationName, boolean enableTranslation, String entityName, String param) {
        String message = enableTranslation ? applicationName + "." + entityName + ".updated" : "A " + entityName + " is updated with identifier " + param;
        return createAlert(applicationName, message, param);
    }

    public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
        return createAlert("A " + entityName + " is deleted with identifier " + param, param);
    }


    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-stockApp-alert", message);
        headers.add("X-stockApp-params", param);
        return headers;
    }
}




