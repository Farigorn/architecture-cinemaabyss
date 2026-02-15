package com.cinemaabyss.events.endpoints.controller;

import com.cinemaabyss.events.DomainEvent;
import com.cinemaabyss.events.endpoints.kafka.EventProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/events")
public class EventsController {

    private final EventProducer producer;

    @Value("${events.topics.user}") private String userTopic;
    @Value("${events.topics.payment}") private String paymentTopic;
    @Value("${events.topics.movie}") private String movieTopic;

    public EventsController(EventProducer producer) {
        this.producer = producer;
    }

    @GetMapping("/health")
    public Map<String, Boolean> health() {
        return Map.of("status", true);
    }

    @PostMapping("/user")
    public ResponseEntity<Map<String, String>> user(@RequestBody(required = false) Map<String, Object> payload) {
        publish("user", userTopic, payload);
        return ResponseEntity.status(201).body(Map.of("status", "success"));
    }


    @PostMapping("/movie")
    public ResponseEntity<Map<String, String>> movie(@RequestBody(required = false) Map<String, Object> payload) {
        publish("movie", movieTopic, payload);
        return ResponseEntity.status(201).body(Map.of("status", "success"));
    }

    @PostMapping("/payment")
    public ResponseEntity<Map<String, String>> payment(@RequestBody(required = false) Map<String, Object> payload) {
        publish("payment", paymentTopic, payload);
        return ResponseEntity.status(201).body(Map.of("status", "success"));
    }



    private void publish(String type, String topic, Map<String, Object> payload) {
        String id = UUID.randomUUID().toString();
        DomainEvent event = new DomainEvent(
                type,
                "created",
                id,
                Instant.now(),
                payload == null ? Map.of() : payload
        );
        producer.send(topic, id, event);
    }
}
