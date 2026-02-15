package com.cinemaabyss.events.endpoints.kafka;

import com.cinemaabyss.events.DomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class EventConsumers {

    private static final Logger log = LoggerFactory.getLogger(EventConsumers.class);

    @KafkaListener(topics = "${events.topics.user}")
    public void onUser(DomainEvent event) {
        log.info("Consumed USER event: {}", event);
    }

    @KafkaListener(topics = "${events.topics.movie}")
    public void onMovie(DomainEvent event) {
        log.info("Consumed MOVIE event: {}", event);
    }

    @KafkaListener(topics = "${events.topics.payment}")
    public void onPayment(DomainEvent event) {
        log.info("Consumed PAYMENT event: {}", event);
    }
}
