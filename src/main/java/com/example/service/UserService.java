package com.example.service;

import com.example.dto.UserDTO;
import com.example.entity.User;
import com.example.event.UserEvent;
import com.example.kafka.KafkaProducerService;
import com.example.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;
    private final KafkaProducerService kafkaProducer;

    public UserService(UserRepository repository, KafkaProducerService kafkaProducer) {
        this.repository = repository;
        this.kafkaProducer = kafkaProducer;
    }

    public UserDTO create(UserDTO dto) {
        User user = repository.save(mapToEntity(dto));
        kafkaProducer.send(new UserEvent(user.getEmail(), "CREATED"));
        return mapToDTO(user);
    }

    public List<UserDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public UserDTO getById(Long id) {
        return repository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow();
    }

    public void delete(Long id) {
        User user = repository.findById(id).orElseThrow();
        repository.deleteById(id);
        kafkaProducer.send(new UserEvent(user.getEmail(), "DELETED"));
    }

    private UserDTO mapToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setAge(user.getAge());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }

    private User mapToEntity(UserDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());
        user.setCreatedAt(dto.getCreatedAt());
        return user;
    }

    
}