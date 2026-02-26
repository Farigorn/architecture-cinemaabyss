package com.cinemaabyss.events;

import java.time.Instant;
import java.util.Map;

public record DomainEvent(
        String type,
        String action,
        String id,
        Instant occurredAt,
        Map<String, Object> payload
) {}