package com.example.demo.cache;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public void init() {
        repository.save(new User("choi", 10));
        repository.save(new User("lee", 15));
        repository.save(new User("park", 20));
    }

    @Cacheable(cacheNames = "user", key = "#id")
    public User getUser(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException());
    }

    @CachePut(cacheNames = "user", key = "#id")
    @Transactional
    public User updateUser(Long id, String name) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException());
    }

    @CacheEvict(cacheNames = "user", key = "#id")
    public void deleteUser(Long id) {
        repository.deleteById(id);
    }
}
