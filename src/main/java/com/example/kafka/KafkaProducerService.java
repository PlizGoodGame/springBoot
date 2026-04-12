package com.example.kafka;

import com.example.event.UserEvent;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, UserEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @CircuitBreaker(name = "kafkaService", fallbackMethod = "fallback")
    public void send(UserEvent event) {
        kafkaTemplate.send("user-topic", event);
    }

    public void fallback(UserEvent event, Throwable t) {
        System.out.println("Kafka недоступна, событие не отправлено: " + event.getEmail());
    }
}