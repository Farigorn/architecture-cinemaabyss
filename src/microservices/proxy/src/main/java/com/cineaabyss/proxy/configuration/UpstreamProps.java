package com.cineaabyss.proxy.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "proxy.upstream")
public record UpstreamProps(String monolithBaseUrl, String moviesBaseUrl) {
}
