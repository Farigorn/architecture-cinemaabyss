package com.cineaabyss.proxy.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "proxy.migration")
public record MigrationProps(int moviesPercent) {
}