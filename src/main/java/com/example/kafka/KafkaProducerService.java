package com.example.kafka;

import com.example.event.UserEvent;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, UserEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(UserEvent event) {
        kafkaTemplate.send("user-topic", event);
    }
}