package com.cineaabyss.proxy.endpoints.controller;

import com.cineaabyss.proxy.client.ProxyClient;
import com.cineaabyss.proxy.configuration.MigrationProps;
import com.cineaabyss.proxy.configuration.UpstreamProps;
import com.cineaabyss.proxy.utils.TrafficDecider;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

@RestController
public class ProxyController {

    private static final Logger log = LoggerFactory.getLogger(ProxyController.class);
    private final MigrationProps migrationProps;

    private final UpstreamProps upstreamProps;

    private final ProxyClient proxyClient;

    private final TrafficDecider trafficDecider;

    public ProxyController(MigrationProps migrationProps, UpstreamProps upstreamProps, ProxyClient proxyClient, TrafficDecider trafficDecider) {
        this.migrationProps = migrationProps;
        this.upstreamProps = upstreamProps;
        this.proxyClient = proxyClient;
        this.trafficDecider = trafficDecider;
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("ok");
    }


    @GetMapping("/api/users")
    public ResponseEntity<byte[]> users(
            @RequestParam(value = "id", required = false) String id,
            HttpServletRequest request
    ) {
        String base = upstreamProps.monolithBaseUrl();
        String url = base + "/api/users" +
                (id != null ? "?id=" + UriUtils.encodeQueryParam(id, StandardCharsets.UTF_8) : "");
        return proxyClient.get(url, request);
    }

    @PostMapping("/api/users")
    public ResponseEntity<byte[]> createUsers(@RequestBody byte[] body, HttpServletRequest request) {
        String url = upstreamProps.monolithBaseUrl() + "/api/users";
        return proxyClient.post(url, body, request);
    }

    @PostMapping("/api/movies")
    public ResponseEntity<byte[]> createMovie(@RequestBody byte[] body, HttpServletRequest request) {
        String url = pickMoviesBase(request) + "/api/movies";
        return proxyClient.post(url, body, request);
    }

    @GetMapping("/api/movies")
    public ResponseEntity<byte[]> movies(
            @RequestParam(value = "id", required = false) String id,
            HttpServletRequest request
    ) {
        String base = pickMoviesBase(request);
        String url = base + "/api/movies" + (id != null ? "?id=" + UriUtils.encodeQueryParam(id, StandardCharsets.UTF_8) : "");
        return proxyClient.get(url, request);
    }

    @GetMapping("/api/payments")
    public ResponseEntity<byte[]> paymentById(@RequestParam("id") String id, HttpServletRequest request) {
        String url = upstreamProps.monolithBaseUrl() + "/api/payments?id=" + UriUtils.encodeQueryParam(id, StandardCharsets.UTF_8);
        return proxyClient.get(url, request);
    }

    @PostMapping("/api/payments")
    public ResponseEntity<byte[]> createPayment(@RequestBody byte[] body, HttpServletRequest request) {
        String url = upstreamProps.monolithBaseUrl() + "/api/payments";
        return proxyClient.post(url, body, request);
    }

    @GetMapping("/api/subscriptions")
    public ResponseEntity<byte[]> subscriptionById(@RequestParam("id") String id, HttpServletRequest request) {
        String url = upstreamProps.monolithBaseUrl() + "/api/subscriptions?id=" + UriUtils.encodeQueryParam(id, StandardCharsets.UTF_8);
        return proxyClient.get(url, request);
    }

    @PostMapping("/api/subscriptions")
    public ResponseEntity<byte[]> createSubscription(@RequestBody byte[] body, HttpServletRequest request) {
        String url = upstreamProps.monolithBaseUrl() + "/api/subscriptions";
        return proxyClient.post(url, body, request);
    }

    private String pickMoviesBase(HttpServletRequest request) {
        int percent = migrationProps.moviesPercent();
        boolean toMovies = trafficDecider.routeToMovies(request, percent);
        String base = toMovies ? upstreamProps.moviesBaseUrl() : upstreamProps.monolithBaseUrl();

        log.info("moviesPercent={}, toMovies={}, base={}", percent, toMovies, base);
        return base;
    }
}
