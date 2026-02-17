package com.cineaabyss.proxy.client;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ProxyClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<byte[]> get(String url, HttpServletRequest inbound) {
        return exchange(url, HttpMethod.GET, null, inbound);
    }

    public ResponseEntity<byte[]> post(String url, byte[] body, HttpServletRequest inbound) {
        return exchange(url, HttpMethod.POST, body, inbound);
    }


    private ResponseEntity<byte[]> exchange(
            String url,
            HttpMethod method,
            byte[] body,
            HttpServletRequest inbound
    ) {
        HttpHeaders headers = new HttpHeaders();

        String contentType = inbound.getHeader(HttpHeaders.CONTENT_TYPE);
        if (contentType != null) headers.set(HttpHeaders.CONTENT_TYPE, contentType);

        String reqId = inbound.getHeader("X-Request-Id");
        if (reqId != null) headers.set("X-Request-Id", reqId);

        HttpEntity<byte[]> entity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(url, method, entity, byte[].class);
    }
}
