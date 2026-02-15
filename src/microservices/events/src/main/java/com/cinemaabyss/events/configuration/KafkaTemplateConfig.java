package com.cinemaabyss.events.configuration;

import com.cinemaabyss.events.DomainEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;

@EnableKafka
@Configuration
public class KafkaTemplateConfig {

    @Bean
    KafkaTemplate<String, DomainEvent> domainEventKafkaTemplate(
            org.springframework.kafka.core.ProducerFactory<String, DomainEvent> pf) {
        return new org.springframework.kafka.core.KafkaTemplate<>(pf);
    }
}
