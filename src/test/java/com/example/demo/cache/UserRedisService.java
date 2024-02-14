package com.example.demo.cache;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRedisService {

    private final UserRedisRepository repository;

    public void init() {
        repository.save(new User("choi", 10));
        repository.save(new User("lee", 15));
        repository.save(new User("park", 20));
    }

    public User getUser(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException());
    }

    public User updateUser(Long id, String name) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException());
    }

    public void deleteUser(Long id) {
        repository.deleteById(id);
    }
}
