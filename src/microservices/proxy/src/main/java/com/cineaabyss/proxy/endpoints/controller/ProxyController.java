package com.cineaabyss.proxy.endpoints.controller;

import com.cineaabyss.proxy.client.ProxyClient;
import com.cineaabyss.proxy.configuration.MigrationProps;
import com.cineaabyss.proxy.configuration.UpstreamProps;
import com.cineaabyss.proxy.utils.TrafficDecider;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("api/users")
    public ResponseEntity<byte[]> users() {
        String url = upstreamProps.monolithBaseUrl() + "/api/users";
        return proxyClient.get(url);
    }

    @GetMapping("api/movies")
    public ResponseEntity<byte[]> movies(HttpServletRequest request) {
        int percent = migrationProps.moviesPercent();
        log.info("percent = {}", percent);
        boolean toMovies = trafficDecider.routeToMovies(request, percent);
        log.info("toMovies = {}", toMovies);
        String base = toMovies
                ? upstreamProps.moviesBaseUrl()
                : upstreamProps.monolithBaseUrl();
        String url = base + "/api/movies";
        log.info("url = {}", url);
        return proxyClient.get(url);
    }
}
