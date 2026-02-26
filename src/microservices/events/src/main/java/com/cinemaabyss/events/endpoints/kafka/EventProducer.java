package com.cinemaabyss.events.endpoints.kafka;

import com.cinemaabyss.events.DomainEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {
    private  final KafkaTemplate<String, DomainEvent> kafkaTemplate;

    public EventProducer(KafkaTemplate<String, DomainEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public  void send(String topic, String key, DomainEvent domainEvent) {
        kafkaTemplate.send(topic, key, domainEvent);
    }
}
